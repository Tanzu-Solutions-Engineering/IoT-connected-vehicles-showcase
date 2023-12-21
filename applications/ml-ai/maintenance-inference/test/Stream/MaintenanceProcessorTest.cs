using System;
using Microsoft.Extensions.Logging;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Prediction;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream;
using Steeltoe.Messaging.RabbitMQ.Core;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.test.Stream
{
    [TestClass]
    public class MaintenanceProcessorTest
    {
        private readonly float probability = 2;
        private readonly float score = 5;
        private CarMaintenanceDto? carMaintenance;
        private MaintenanceProcessor? subject;
        private Mock<IPredictor>? predictor;
        private Mock<ILogger<MaintenanceProcessor>>? logger;
        private MaintenanceDto? expected;
        private Mock<IRabbitTemplate>? rabbitTemplate;

        [TestInitialize]
        public void InitializeMaintenanceProcessorTest()
        {
            rabbitTemplate = new Mock<IRabbitTemplate>();

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

            subject = new MaintenanceProcessor(predictor.Object,logger.Object,rabbitTemplate.Object);
        }
        [TestMethod]
        public void Predict()
        {
            predictor.Setup(p=> p.Predict(It.IsAny<CarMaintenanceDto>())).Returns(expected);
            
            subject.Predict(carMaintenance);

           rabbitTemplate.Verify( t => t.ConvertAndSend(It.IsAny<String>(),It.IsAny<String>(),It.IsAny<MaintenanceDto>()));

        }
    }
}