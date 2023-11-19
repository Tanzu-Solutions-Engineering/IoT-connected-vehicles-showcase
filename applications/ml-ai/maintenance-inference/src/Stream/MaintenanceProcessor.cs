
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Steeltoe.Messaging.Handler.Attributes;
using Steeltoe.Stream.Attributes;
using Steeltoe.Stream.Messaging;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream
{
    [EnableBinding(typeof(IProcessor))]
    public class MaintenanceProcessor
    {
         [StreamListener(IProcessor.INPUT)]
        [SendTo(IProcessor.OUTPUT)]
        public MaintenanceDto? Predict(CarMaintenanceDto carMaintenanceDto)
        {
            return new MaintenanceDto();
        }
    }
}