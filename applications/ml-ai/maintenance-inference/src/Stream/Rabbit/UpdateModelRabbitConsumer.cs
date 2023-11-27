using System;
using System.Collections.Generic;
using Imani.Solutions.Core.API.Util;
using OpenTelemetry.Trace;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream.Rabbit
{
    public class UpdateModelRabbitConsumer
    {
        public static readonly string DEFAULT_TRAINED_MODEL_QUEUE_NM = "vehicle.trained.model";

        public static readonly string RABBIT_URI_PROP_NM = "RabbitMQ_URI";
        private static readonly string applicationName = "maintenance-inference";

        private string queueName;
        private IConnection conn;
        private IModel channel;
        private readonly UpdateModelConsumer updateModelConsumer;
        private static readonly string DEFAULT_EXCHANGE_NM = DEFAULT_TRAINED_MODEL_QUEUE_NM;
        private static readonly string DEFAULT_ROUTING_KEY_RULE = "#";


        private readonly IConnectionFactory factory;
        private readonly string exchangeName;

        public UpdateModelRabbitConsumer(UpdateModelConsumer updateModelConsumer,
                                         ISettings config)
                                         : this(new ConnectionFactory(),updateModelConsumer,config)
        {
        }
        public UpdateModelRabbitConsumer(IConnectionFactory factory, UpdateModelConsumer updateModelConsumer, ISettings config)
        {
            this.factory = factory;
            this.updateModelConsumer = updateModelConsumer;

            
            factory.ClientProperties["connection_name"] = applicationName;

            var uri = config.GetProperty(RABBIT_URI_PROP_NM,"amqp://@localhost:5672");

            factory.Uri = new Uri(uriString: uri);

            queueName = config.GetProperty("RabbitMQ_Queue",DEFAULT_TRAINED_MODEL_QUEUE_NM);

            Console.WriteLine($"**** Reading queue:{queueName} ");

            conn = factory.CreateConnection();
            channel = conn.CreateModel();

            IDictionary<string, object> queueArgs = new Dictionary<string, object>();
            queueArgs["x-queue-type"] = "stream";
            


            exchangeName = config.GetProperty("UPDATE_MODEL_EXCHANGE_NM",DEFAULT_EXCHANGE_NM);
            
            channel.ExchangeDeclare(
                exchange: exchangeName,
                type: "topic",
                durable: true,
                autoDelete: false);

            var response = channel.QueueDeclare(
                queue: queueName, 
                durable: true,
                exclusive: false,
                autoDelete: false,
                arguments: queueArgs );


            channel.QueueBind(queueName,exchangeName,routingKey: DEFAULT_ROUTING_KEY_RULE);


            channel.BasicQos(prefetchSize: 0, prefetchCount: 10000, global: false);
            
            var consumer = new EventingBasicConsumer(channel);
            consumer.Received += (ch, ea) =>
                {
                    this.updateModelConsumer.UpdateModel(ea.Body);
                    channel.BasicAck(ea.DeliveryTag, false);
                };

            IDictionary<string, object> consumerArgs = new Dictionary<string,object>();

            consumerArgs["x-stream-offset"] = "first";
            //"x-stream-offset"

            string consumerTag = channel.BasicConsume(queue: queueName, 
                                                    autoAck: false, 
                                                    exclusive: true,
                                                    arguments : consumerArgs, 
                                                    consumer: consumer);
        }
        
    }
}