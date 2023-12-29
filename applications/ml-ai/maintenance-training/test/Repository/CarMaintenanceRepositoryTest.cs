using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.Repository;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.src.Repository;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.test.Repository
{
    [TestClass]
    public class CarMaintenanceRepositoryTest
    {
        private CarMaintenanceRepository subject;
        private DbContextOptions<CarMaintenanceDbContext> options;
        private CarMaintenanceDbContext dbContext;
        private Mock<ILogger<CarMaintenanceRepository>> logger;

        public SaveCarMaintenanceRecord carMaintenanceRecord;
        private CarMaintenance carMaintenance;
        private int carMaintenanceCount = 1;
        private string schema = "myschema";
        private string connectionString = "InsideDB";

        [TestInitialize]
        public void InitializeCarMaintenanceRepositoryTest()
        {
            carMaintenance = new CarMaintenance();
            carMaintenance.slno  =23;
            
            carMaintenanceRecord = new SaveCarMaintenanceRecord(
                carMaintenance,carMaintenanceCount);

            logger = new Mock<ILogger<CarMaintenanceRepository>>();

            options =  new DbContextOptionsBuilder<CarMaintenanceDbContext>()
            .UseInMemoryDatabase(databaseName: "SampleDatabase")
            .Options;


            var configSettings = new Dictionary<string, string>
            {
                {"schemaName", schema},
                {"AppSettings:SmsApi", "http://example.com"}
            };

            var configuration = new ConfigurationBuilder()
            .AddInMemoryCollection(configSettings)
            .Build();

            dbContext = new CarMaintenanceDbContext(configuration,options);
            this.subject = new CarMaintenanceRepository(logger.Object,dbContext);
        }
        
        [TestMethod]
        public void Save()
        {

            subject.Save(carMaintenanceRecord);

            Assert.IsTrue(subject.Count() >= carMaintenanceCount);
        }
    }
}