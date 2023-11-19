using System.IO;
using Microsoft.ML;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Prediction;
using Steeltoe.Stream.Attributes;
using Steeltoe.Stream.Messaging;


namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream
{
    [EnableBinding(typeof(ISink))]
    public class UpdateModelConsumer
    {
        private IPredictor predictor;
        private readonly MLContext mLContext;

        public UpdateModelConsumer(IPredictor predictor)
        {
            mLContext = new MLContext();
            this.predictor = predictor;
        }

        // [StreamListener(ISink.INPUT)]
        [StreamListener(ISink.INPUT, "Headers['type']=='Dog'")]
        // [StreamListener("UpdateModel")]
        public void UpdateModel(byte[] model)
        {
            DataViewSchema modelSchema;

            MemoryStream stream = new MemoryStream(model);

            ITransformer newModel = mLContext.Model.Load(stream,out modelSchema);
            predictor.TrainedModel = newModel;
        }
    }
}