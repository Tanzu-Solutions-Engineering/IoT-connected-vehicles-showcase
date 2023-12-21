using System;
using Microsoft.Extensions.Logging;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Prediction;
using Steeltoe.Messaging.RabbitMQ.Core;
using Steeltoe.Stream.Attributes;
using Steeltoe.Stream.Messaging;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream
{
    [EnableBinding(typeof(ISink))]
    public class MaintenanceProcessor
    {
        private readonly IPredictor predictor;
        private readonly ILogger logger;
        private readonly IRabbitTemplate rabbitTemplate;

        private readonly string sendExchange = "vehicle.maintenance.prediction";

        public MaintenanceProcessor(IPredictor predictor, 
                                    ILogger<MaintenanceProcessor> logger, 
                                    IRabbitTemplate rabbitTemplate)
        {
            this.predictor = predictor;
            this.logger = logger;
            this.rabbitTemplate = rabbitTemplate;
        }

        [StreamListener(ISink.INPUT)]
        public void Predict(CarMaintenanceDto carMaintenanceDto)
        {

            Console.WriteLine($"GOT: {carMaintenanceDto}");
            var maintenanceDto = predictor.Predict(carMaintenanceDto);

            Console.WriteLine($"SENDING:{maintenanceDto}");

            this.rabbitTemplate.ConvertAndSend(sendExchange,maintenanceDto.vin,maintenanceDto);
        }

        // private void send(MaintenanceDto maintenanceDto)
        // {
        //       using(var connectionFactory = new CachingConnectionFactory()
        //                 {
        //                     Host = "localhost"
        //                 })
        //     {
        //         var template = new RabbitTemplate(connectionFactory);
        //         template.MessageConverter = new NewtonJsonMessageConverter();
        //         template.ConvertAndSend(sendExchange,maintenanceDto.vin,maintenanceDto);
        //     }
        // }
    }
}