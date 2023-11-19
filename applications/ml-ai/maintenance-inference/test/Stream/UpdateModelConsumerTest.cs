using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Security.Cryptography.X509Certificates;
using System.Threading.Tasks;
using Microsoft.ML;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Stream;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.src.Stream;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.test.Stream
{
    [TestClass]
    public class UpdateModelConsumerTest
    {
        

        private UpdateModelConsumer subject;
        private MaintenanceProcessor processor;
        private ITransformer trainedModel;

        private static string fileName = "/Users/Projects/VMware/Tanzu/Use-Cases/IoT/dev/IoT-connected-vehicles-showcase/applications/ml-ai/maintenance-inference/runtime/model.zip";
        private byte[] model = File.ReadAllBytes(fileName);

        [TestInitialize]
        public void InitializeUpdateModelConsumerTest()
        {
            DataViewSchema modelSchema;
            
            trainedModel = new MLContext().Model.Load(fileName, out modelSchema);

            processor = new MaintenanceProcessor();

            subject = new UpdateModelConsumer(processor);
        }

        [TestMethod]
        public void UpdateModel()
        {

            processor.TrainedModel = trainedModel;

            subject.UpdateModel(model);

            Assert.IsNotNull(processor.TrainedModel);
        }


        

    }
}