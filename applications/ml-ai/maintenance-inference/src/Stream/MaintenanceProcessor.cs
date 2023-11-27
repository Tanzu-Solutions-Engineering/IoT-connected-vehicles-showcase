
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

        public MaintenanceProcessor(IPredictor predictor)
        {
            this.predictor = predictor;
        }


        [StreamListener(IProcessor.INPUT)]
        [SendTo(IProcessor.OUTPUT)]
        public MaintenanceDto? Predict(CarMaintenanceDto carMaintenanceDto)
        {
            return predictor.Predict(carMaintenanceDto);
        }
    }
}