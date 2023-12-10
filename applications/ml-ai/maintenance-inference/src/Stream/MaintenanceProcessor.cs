
using System;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Prediction;
using Steeltoe.Messaging.Handler.Attributes;
using Steeltoe.Stream.Attributes;
using Steeltoe.Stream.Messaging;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream
{
    [EnableBinding(typeof(IProcessor))]
    public class MaintenanceProcessor
    {
        private readonly IPredictor predictor;
        private readonly ILogger logger;

        public MaintenanceProcessor(IPredictor predictor, ILogger<MaintenanceProcessor> logger)
        {
            this.predictor = predictor;
            this.logger = logger;
        }


        [StreamListener(IProcessor.INPUT)]
        [SendTo(IProcessor.OUTPUT)]
        public MaintenanceDto Predict(CarMaintenanceDto carMaintenanceDto)
        {
            logger.LogInformation($"*****  Predict dto: {carMaintenanceDto}");
            var maintenanceDto = predictor.Predict(carMaintenanceDto);

            logger.LogInformation($"*****  RETURNING Maintenance DTO: {maintenanceDto}");
            return maintenanceDto;
        }
    }
}