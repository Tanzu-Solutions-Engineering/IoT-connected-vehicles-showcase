using System;
using Imani.Solutions.Core.API.Serialization;
using Microsoft.Extensions.Logging;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Prediction;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.test.Stream
{
    [TestClass]
    public class MaintenanceProcessorTest
    {
        private readonly float probability = 2;
        private readonly float score = 5;
        private CarMaintenanceDto carMaintenance;
        private MaintenanceProcessor subject;
        private Mock<IPredictor> predictor;
        private Mock<ILogger<MaintenanceProcessor>> logger;
        private MaintenanceDto expected;

        [TestInitialize]
        public void InitializeMaintenanceProcessorTest()
        {
            predictor = new Mock<IPredictor>();
            logger = new Mock<ILogger<MaintenanceProcessor>>();
            carMaintenance = new CarMaintenanceDto();
            carMaintenance.vin = "12";


            expected = new MaintenanceDto();
            expected.vin = carMaintenance.vin;
            expected.prediction = new MaintenancePrediction();
            expected.prediction.Prediction = true;
            expected.prediction.Probability = probability;
            expected.prediction.Score = score;


            subject = new MaintenanceProcessor(predictor.Object,logger.Object);
        }
        [TestMethod]
        public void Predict()
        {
            predictor.Setup(p=> p.Predict(It.IsAny<CarMaintenanceDto>())).Returns(expected);
            
            var actual = subject.Predict(carMaintenance);

            Assert.IsNotNull(actual);

            Assert.AreEqual(actual.vin,carMaintenance.vin);

            JsonSerde<MaintenanceDto> jsonSerde = new JsonSerde<MaintenanceDto>();
            var json = jsonSerde.Serialize(actual);

            Console.WriteLine($"OUTPUT: {json}");

            Assert.IsTrue(json.Contains(carMaintenance.vin));

        }
    }
}