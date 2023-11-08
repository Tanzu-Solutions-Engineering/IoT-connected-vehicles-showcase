using System;
using System.Collections.Generic;
using Microsoft.Extensions.Logging;
using Microsoft.ML;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.ML;
using Steeltoe.Integration.Rabbit.Support;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.test.ML
{
    [TestClass]
    public class MicrosoftMlTrainerTest
    {
        private MicrosoftMlTrainer subject;
        private LoadDataView dvLoader;
        private System.IO.Stream ModelPath;
        private string TestDataPath = "/Users/Projects/VMware/Tanzu/Use-Cases/IoT/dev/IoT-connected-vehicles-showcase/applications/ml-ai/predictive-maintenance/test/resources/car-predictive-test-data.csv";
        private Mock<ILogger> logger;

        [TestInitialize]
        public void InitializeMicrosoftMlTrainerTest()
        {
            logger = new Mock<ILogger>();
            
             dvLoader = mlContext => mlContext.Data.LoadFromTextFile<CarMaintenance>(TestDataPath, hasHeader: true, separatorChar: ';');
            subject = new MicrosoftMlTrainer(dvLoader);
        }

        [TestMethod]
        public void Train()
        {
            var actual = subject.Train();

            Assert.IsNotNull(actual);
        }
    }
}