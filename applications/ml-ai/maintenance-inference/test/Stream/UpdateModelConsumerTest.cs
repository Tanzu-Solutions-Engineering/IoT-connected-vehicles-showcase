using System.IO;
using Microsoft.ML;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Prediction;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.test.Stream
{
    [TestClass]
    public class UpdateModelConsumerTest
    {

        private UpdateModelConsumer subject;
        private MaintenancePredictor processor;
        private ITransformer trainedModel;

        private static string fileName = "/Users/Projects/VMware/Tanzu/Use-Cases/IoT/dev/IoT-connected-vehicles-showcase/applications/ml-ai/maintenance-inference/runtime/model.zip";
        private byte[] model = File.ReadAllBytes(fileName);

        [TestInitialize]
        public void InitializeUpdateModelConsumerTest()
        {
            DataViewSchema modelSchema;
            
            trainedModel = new MLContext().Model.Load(fileName, out modelSchema);

            processor = new MaintenancePredictor();

            subject = new UpdateModelConsumer(processor);
        }

        [TestMethod]
        public void UpdateModel()
        {

            processor.TrainedModel = null;

            subject.UpdateModel(model);

            Assert.IsNotNull(processor.TrainedModel);
        }


    }
}