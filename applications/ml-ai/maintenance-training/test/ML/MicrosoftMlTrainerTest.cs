using System;
using System.Diagnostics;
using System.IO;
using Microsoft.Extensions.Logging;
using Microsoft.ML;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using Serilog;
using Serilog.Core;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.ML;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.test.ML
{
    [TestClass]
    public class MicrosoftMlTrainerTest
    {
        private Logger? debugger;
        private Microsoft.Extensions.Logging.ILogger? logger;
    
        private MicrosoftMlTrainer? subject;
        private LoadDataView? dvLoader;
        // private System.IO.Stream ModelPath;
        private string TestDataPath = "/Users/Projects/VMware/Tanzu/Use-Cases/IoT/dev/IoT-connected-vehicles-showcase/applications/ml-ai/maintenance-training/test/resources/car-predictive-test-data.csv";
                                         
        private string logFile = "/Users/Projects/VMware/Tanzu/Use-Cases/IoT/dev/IoT-connected-vehicles-showcase/applications/ml-ai/maintenance-training/runtime/predictive-maintenance.log";

        // private Mock<ILogger> logger;

        [TestInitialize]
        public void InitializeMicrosoftMlTrainerTest()
        {
            debugger = new LoggerConfiguration()
            .MinimumLevel.Debug()
            .WriteTo.Console()
            .WriteTo.File(logFile, rollingInterval: RollingInterval.Day)
            .CreateLogger();

            logger = LoggerFactory.Create(builder => builder.AddConsole()).CreateLogger("Program");
            
             dvLoader = mlContext => mlContext.Data.LoadFromTextFile<CarMaintenance>(TestDataPath, hasHeader: true, separatorChar: ',');
            subject = new MicrosoftMlTrainer(dvLoader);
        }



        [TestMethod]
        public void Train()
        {
            
            var actual = subject.Train();
            Assert.IsNotNull(actual);

            string fileName = "/Users/Projects/VMware/Tanzu/Use-Cases/IoT/dev/IoT-connected-vehicles-showcase/applications/ml-ai/maintenance-training/runtime/model.zip";

            //writer to to file
            using (var fileStream = File.Open(fileName, FileMode.Create))
            {
                using (var writer = new BinaryWriter(fileStream))
                {
                    writer.Write(actual);
                }
            }

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
            
            
            


            //Get Model
            var mlContext = new MLContext();
            System.IO.Stream stream = new MemoryStream(actual);
           
           //Define DataViewSchema for data preparation pipeline and trained model
            DataViewSchema modelSchema;

            // Load trained model
            ITransformer trainedModel = mlContext.Model.Load(fileName, out modelSchema);
           

           //Predict
            var predictionEngine = mlContext.Model.CreatePredictionEngine<CarMaintenance, MaintenancePrediction>(trainedModel);


            var prediction = predictionEngine.Predict(carMaintenance);


            debugger.Information($"=============== Single Prediction  ===============");
            debugger.Information($"Prediction Score: {prediction.Score} ");
            debugger.Information($"Prediction: {(prediction.Prediction)}");
            debugger.Information($"Probability: {prediction.Probability} ");
            debugger.Information($"==================================================");

            Assert.IsTrue(prediction.Prediction," Must need maintenance");
        }
    }
}