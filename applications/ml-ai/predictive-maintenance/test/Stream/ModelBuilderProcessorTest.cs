using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization.Infrastructure;
using Microsoft.Extensions.Logging;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.ML;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Stream;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.test
{
    [TestClass]
    public class ModelBuilderProcessorTest
    {
        private Mock<ITrainer> trainer;
        private ModelBuilderProcessor subject;
        private  Mock<ILogger<ModelBuilderProcessor>> logger;

        [TestInitialize]
        public void InitializeModelBuilderProcessorTest()
        {
            trainer = new Mock<ITrainer>();
            logger = new Mock<ILogger<ModelBuilderProcessor>>();

            subject = new ModelBuilderProcessor(trainer.Object);
        }

        [TestMethod]
        public void BuildModel()
        {
            byte[] expected = { 1,2,3};
            trainer.Setup( t => t.Train()).Returns(expected);


            string args = "hello";
            var actual = subject.BuildModel(args);

            
            Assert.AreEqual(actual,expected);
        }
    }
}