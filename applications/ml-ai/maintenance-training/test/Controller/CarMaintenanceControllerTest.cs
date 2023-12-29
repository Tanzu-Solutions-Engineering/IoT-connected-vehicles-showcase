using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection.Emit;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.Repository;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.src.Controllers;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.test.Controller
{
    [TestClass]
    public class CarMaintenanceControllerTest
    {
        private readonly float brand = 2;
        private readonly float region = 3;
        private readonly bool label = true;
        private readonly float make_year = 5;
        private SaveCarMaintenanceRecord saveRecord;
        private CarMaintenance carMaintenance;
        private int count = 14;
        private readonly float model = 6;

        private Mock<ICarMaintenanceRepository> repository;
        private Mock<ILogger<CarMaintenanceController>> logger;

        private CarMaintenanceController subject;

        [TestInitialize]
        public void InitializeCarMaintenanceControllerTest()
        {
            logger = new Mock<ILogger<CarMaintenanceController>>();
            carMaintenance = new CarMaintenance();
            carMaintenance.brand = brand;
            carMaintenance.region = region;
            carMaintenance.label = label;
            carMaintenance.make_year = make_year;
            carMaintenance.model = model;
            carMaintenance.label = label;

            repository = new Mock<ICarMaintenanceRepository>();

            this.saveRecord = new SaveCarMaintenanceRecord(carMaintenance,count);

            this.subject = new CarMaintenanceController(repository.Object, logger.Object);
            
        }
        
        [TestMethod]
        public void SaveCarMaintence()
        {
            subject.SaveCarMaintenance(saveRecord);

            repository.Verify(r => r.Save(saveRecord));
            
        }
    }
}