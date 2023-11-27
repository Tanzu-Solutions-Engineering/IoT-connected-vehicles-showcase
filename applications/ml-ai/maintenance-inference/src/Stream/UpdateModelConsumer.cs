using System;
using System.IO;
using Microsoft.ML;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Prediction;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream
{
    public class UpdateModelConsumer
    {
        private IPredictor predictor;
        private readonly MLContext mLContext;

        public UpdateModelConsumer(IPredictor predictor)
        {
            mLContext = new MLContext();
            this.predictor = predictor;
        }

        public void UpdateModel(byte[] model)
        {
            Console.WriteLine($"**** Updating model {model}");
            
            DataViewSchema modelSchema;

            MemoryStream stream = new MemoryStream(model);

            ITransformer newModel = mLContext.Model.Load(stream,out modelSchema);
            predictor.TrainedModel = newModel;
        }
    }
}