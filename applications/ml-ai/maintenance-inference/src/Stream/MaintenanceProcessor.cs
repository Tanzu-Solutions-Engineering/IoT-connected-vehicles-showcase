
using System;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Prediction;
using Steeltoe.Messaging.Converter;
using Steeltoe.Messaging.Handler.Attributes;
using Steeltoe.Messaging.RabbitMQ.Connection;
using Steeltoe.Messaging.RabbitMQ.Core;
using Steeltoe.Stream.Attributes;
using Steeltoe.Stream.Messaging;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream
{

    // [EnableBinding(typeof(IProcessor))]
    [EnableBinding(typeof(ISink))]
    public class MaintenanceProcessor
    {
        private readonly IPredictor predictor;
        private readonly ILogger logger;

        private readonly string sendExchange = "vehicle.maintenance.prediction";

        public MaintenanceProcessor(IPredictor predictor, ILogger<MaintenanceProcessor> logger)
        {
            this.predictor = predictor;
            this.logger = logger;
        }


        // [StreamListener(IProcessor.INPUT)]
        // [SendTo(IProcessor.OUTPUT)]
        [StreamListener(ISink.INPUT)]
        public void Predict(CarMaintenanceDto carMaintenanceDto)
        {
            // logger.LogInformation($"*****  Predict dto: {carMaintenanceDto}");
            var maintenanceDto = predictor.Predict(carMaintenanceDto);


            send(maintenanceDto);

            // logger.LogInformation($"*****  RETURNING Maintenance DTO: {maintenanceDto}");
           //return maintenanceDto;
        }

        private void send(MaintenanceDto maintenanceDto)
        {
              using(var connectionFactory = new CachingConnectionFactory()
                        {
                            Host = "localhost"
                        }
                    )
            {
                var template = new RabbitTemplate(connectionFactory);
                template.MessageConverter = new NewtonJsonMessageConverter();
                template.ConvertAndSend(sendExchange,maintenanceDto.vin,maintenanceDto);
            }
        }
    }
}