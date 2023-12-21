using System;
using System.Collections.Generic;
using System.Text;
using Imani.Solutions.Core.API.Serialization;
using Imani.Solutions.Core.API.Util;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;


namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream.Rabbit
{
    public class RabbitStreamBuilder
    {
        public static readonly string DEFAULT_TRAINED_MODEL_QUEUE_NM = "vehicle.trained.model";

        public static readonly string RABBIT_URI_PROP_NM = "RabbitMQ_URI";

        private static readonly string applicationName = "maintenance-inference"; 

        private static readonly string predictionReqeustQueueName = "vehicle.maintenance.predictive.request.predictive-maintenance-processor";

        private string traininModelQueueName;

        private IConnection conn;

        private IModel updateModelChannel;

        private IModel predictMaintenanceRequestChannel;

        private IModel maintenanceChannel;
        private readonly UpdateModelConsumer updateModelConsumer;


        private static readonly string DEFAULT_EXCHANGE_NM = DEFAULT_TRAINED_MODEL_QUEUE_NM;

        private static readonly string DEFAULT_ROUTING_KEY_RULE = "#";

        private readonly static JsonSerde<CarMaintenanceDto> carMaintenanceDtoSerde = new JsonSerde<CarMaintenanceDto>();

        private readonly  static JsonSerde<MaintenanceDto> maintenanceDtoSerde = new JsonSerde<MaintenanceDto>();

        private readonly IConnectionFactory factory;

        private readonly string updateModelExchangeName;


        public RabbitStreamBuilder(UpdateModelConsumer updateModelConsumer,
                                         ISettings config)
                                         : this(new ConnectionFactory(),updateModelConsumer,config)
        {
        }
        public RabbitStreamBuilder(IConnectionFactory factory, UpdateModelConsumer updateModelConsumer, ISettings config)
        {
            
            this.factory = factory;
            this.updateModelConsumer = updateModelConsumer;
            
            factory.ClientProperties["connection_name"] = applicationName;

            var uri = config.GetProperty(RABBIT_URI_PROP_NM,"amqp://@localhost:5672");


            factory.Uri = new Uri(uriString: uri);
            traininModelQueueName = config.GetProperty("TRAINING_MODEL_QUEUE_NM",DEFAULT_TRAINED_MODEL_QUEUE_NM);

            updateModelExchangeName = config.GetProperty("UPDATE_MODEL_EXCHANGE_NM",DEFAULT_EXCHANGE_NM);


            conn = factory.CreateConnection();

                  
            ConstructTraining();

            // ConstructPredictMaintenance();
        }

        private void ConstructPredictMaintenance()
        {

            IDictionary<string, object> queueArgs = new Dictionary<string, object>();

            queueArgs["x-queue-type"] = "quorum";

            
            this.predictMaintenanceRequestChannel = conn.CreateModel();

            this.maintenanceChannel = conn.CreateModel();

            
            predictMaintenanceRequestChannel.ExchangeDeclare(
                exchange: "vehicle.maintenance.predictive.request",
                type: "topic",
                durable: true,
                autoDelete: false);


            var response = predictMaintenanceRequestChannel.QueueDeclare(
                queue: predictionReqeustQueueName, 
                durable: true,
                exclusive: false,
                autoDelete: false,
                arguments: queueArgs );



            predictMaintenanceRequestChannel.QueueBind(predictionReqeustQueueName,"vehicle.maintenance.predictive.request",
            routingKey: DEFAULT_ROUTING_KEY_RULE);

            predictMaintenanceRequestChannel.BasicQos(prefetchSize: 0, prefetchCount: 10000, global: false);

            
            var consumer = new EventingBasicConsumer(predictMaintenanceRequestChannel);


            consumer.Received += (ch, ea) =>
                {
                    try{
                        Console.WriteLine("***** Handling prediction Request");

                  var carMaintenanceDto = toCarMaintenanceDto(ea.Body);

                  Console.WriteLine($"***** Got Car maintenance Dto {carMaintenanceDto}");

                    }
                    catch(Exception e)
                    {
                        Console.WriteLine($"ERROR: {e}");
                        throw;
                    }


                    
                };


            IDictionary<string, object> consumerArgs = new Dictionary<string,object>();


            string consumerTag = predictMaintenanceRequestChannel.BasicConsume(queue: predictionReqeustQueueName, 
                                                    autoAck: false, 
                                                    exclusive: true,
                                                    arguments : consumerArgs, 
                                                    consumer: consumer);

        }

        private void ConstructTraining()
        {

            Console.WriteLine($"**** Reading queue:{traininModelQueueName} ");

            IDictionary<string, object> queueArgs = new Dictionary<string, object>();

            queueArgs["x-queue-type"] = "stream";
            
            updateModelChannel = conn.CreateModel();


            updateModelChannel.ExchangeDeclare(
                exchange: updateModelExchangeName,
                type: "topic",
                durable: true,
                autoDelete: false);


            var response = updateModelChannel.QueueDeclare(
                queue: traininModelQueueName, 
                durable: true,
                exclusive: false,
                autoDelete: false,
                arguments: queueArgs );



            updateModelChannel.QueueBind(traininModelQueueName,updateModelExchangeName,routingKey: DEFAULT_ROUTING_KEY_RULE);

            updateModelChannel.BasicQos(prefetchSize: 0, prefetchCount: 10000, global: false);
            
            var consumer = new EventingBasicConsumer(updateModelChannel);

            consumer.Received += (ch, ea) =>
                {
                    this.updateModelConsumer.UpdateModel(ea.Body);

                    updateModelChannel.BasicAck(ea.DeliveryTag, false);

                };


            IDictionary<string, object> consumerArgs = new Dictionary<string,object>();


            consumerArgs["x-stream-offset"] = "first";


            string consumerTag = updateModelChannel.BasicConsume(queue: traininModelQueueName, 
                                                    autoAck: false, 
                                                    exclusive: true,
                                                    arguments : consumerArgs, 
                                                    consumer: consumer);

        }

        internal static CarMaintenanceDto toCarMaintenanceDto(byte[] jsonBytes)
        {
           string payloadText = "";

            try 
            {
                payloadText = Encoding.UTF8.GetString(jsonBytes);
                return carMaintenanceDtoSerde.Deserialize(payloadText);
            }
            catch(Exception e)
            {
              Console.WriteLine($"payloadText: {payloadText} ERROR: {e.StackTrace}");
                throw;
            }
                   
        }
    }
}