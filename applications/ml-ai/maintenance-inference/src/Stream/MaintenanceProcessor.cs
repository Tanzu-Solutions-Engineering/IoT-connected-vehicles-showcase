using Microsoft.ML;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Steeltoe.Messaging.Handler.Attributes;
using Steeltoe.Stream.Attributes;
using Steeltoe.Stream.Messaging;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Stream
{
    [EnableBinding(typeof(IProcessor))]
    public class MaintenanceProcessor
    {
        
        private PredictionEngine<CarMaintenance, MaintenancePrediction>? predictionEngine;
        private readonly MLContext mlContext;

        private ITransformer? trainedModel = null;

        public ITransformer? TrainedModel { 
            get { return trainedModel;}
            set{
                this.trainedModel = value;

                //Predict
                predictionEngine = mlContext.Model.CreatePredictionEngine<CarMaintenance, MaintenancePrediction>(this.trainedModel);

            }
        } 

        public MaintenanceProcessor()
        {
            this.mlContext = new MLContext();

        }

        [StreamListener(IProcessor.INPUT)]
            [SendTo(IProcessor.OUTPUT)]
            public MaintenanceDto? Predict(CarMaintenanceDto carMaintenanceDto)
            {
                if(predictionEngine == null)
                    return null;

                 var prediction = predictionEngine.Predict(carMaintenanceDto.carMaintenance);

                 var maintenanceDto = new MaintenanceDto();
                 maintenanceDto.vin = carMaintenanceDto.vin;
                 maintenanceDto.prediction = prediction;
                return maintenanceDto;
            }
        
    }
}