using System;
using System.Diagnostics;
using System.IO;
using Microsoft.AspNetCore.Mvc.Diagnostics;
using Microsoft.Extensions.Logging;
using Microsoft.ML;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Serilog;
using Serilog.Core;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Stream;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.test.ML
{
    [TestClass]
    public class MaintenanceProcessorTest
    {
        private Logger debugger;
        private Microsoft.Extensions.Logging.ILogger logger;
    
        private string logFile = "/Users/Projects/VMware/Tanzu/Use-Cases/IoT/dev/IoT-connected-vehicles-showcase/applications/ml-ai/maintenance-inference/runtime/maintenance-interface-test.log";
        string fileName = "/Users/Projects/VMware/Tanzu/Use-Cases/IoT/dev/IoT-connected-vehicles-showcase/applications/ml-ai/maintenance-inference/runtime/model.zip";
                        
        MaintenanceProcessor subject;
        private  ITransformer? trainModel;
        private string vin = "123";

        [TestInitialize]
        public void InitializeMicrosoftMlTrainerTest()
        {
            debugger = new LoggerConfiguration()
            .MinimumLevel.Debug()
            .WriteTo.Console()
            .WriteTo.File(logFile, rollingInterval: RollingInterval.Day)
            .CreateLogger();

            logger = LoggerFactory.Create(builder => builder.AddConsole()).CreateLogger("Program");

            MLContext mlContext = new MLContext();
            //Define DataViewSchema for data preparation pipeline and trained model
            DataViewSchema modelSchema;

            // Load trained model
           trainModel = mlContext.Model.Load(fileName, out modelSchema);

            subject = new MaintenanceProcessor(trainModel);
            
        }

        public MaintenanceProcessor? GetSubject()
        {
            return subject;
        }

        [TestMethod]
        public void Predict()
        {
            
            CarMaintenance carMaintenance = new CarMaintenance();
            carMaintenance.vehicle_type = 1;
            carMaintenance.slno = 27;
            carMaintenance.brand = 1;
            carMaintenance.model = 1;
            carMaintenance.engine_type = 1;
            carMaintenance.make_year = 2017;
            carMaintenance.region = 1;
            carMaintenance.mileage_range = 10000;
            carMaintenance.mileage = 11400;


            debugger.Information($"{carMaintenance}");

            CarMaintenanceDto carMaintenanceDto = new CarMaintenanceDto(vin,carMaintenance);
            
            var actual = subject.Predict(carMaintenanceDto);

            Assert.IsNotNull(actual);

            Assert.AreEqual(vin,actual.vin);
            Assert.AreEqual(true,actual.prediction.Prediction);

        }
    }
}