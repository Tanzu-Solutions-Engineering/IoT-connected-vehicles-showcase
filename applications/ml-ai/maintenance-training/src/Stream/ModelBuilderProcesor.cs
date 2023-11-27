using System;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.ML;
using Steeltoe.Messaging.Handler.Attributes;
using Steeltoe.Stream.Attributes;
using Steeltoe.Stream.Messaging;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Stream
{
    [EnableBinding(typeof(IProcessor))]
    public class ModelBuilderProcessor
    {
        private ITrainer trainer;

        public ModelBuilderProcessor(ITrainer trainer)
        {
            this.trainer = trainer;
        }

        [StreamListener(IProcessor.INPUT)]
            [SendTo(IProcessor.OUTPUT)]
            public byte[] BuildModel(string args)
            {
                Console.WriteLine($"args: {args}");
                return trainer.Train();

            }
        
    }
}