using Microsoft.ML;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Steeltoe.Messaging.Handler.Attributes;
using Steeltoe.Stream.Attributes;
using Steeltoe.Stream.Messaging;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Prediction
{
    
    public class MaintenancePredictor : IPredictor
    {

        private PredictionEngine<CarMaintenance, MaintenancePrediction>? predictionEngine;
        private readonly MLContext mlContext;

        private ITransformer? trainedModel = null;

        /// <summary>
        /// Constructor for the predictor
        /// </summary>
        public MaintenancePredictor()
        {
            this.mlContext = new MLContext();

        }

        public ITransformer? TrainedModel
        {
            get { return trainedModel; }
            set
            {
                this.trainedModel = value;

                if (this.trainedModel != null)
                {
                    //Predict
                    predictionEngine = mlContext.Model.CreatePredictionEngine<CarMaintenance, MaintenancePrediction>(this.trainedModel);
                }
            }
        }

        [StreamListener(IProcessor.INPUT)]
        [SendTo(IProcessor.OUTPUT)]
        public MaintenanceDto? Predict(CarMaintenanceDto carMaintenanceDto)
        {
            if (predictionEngine == null || carMaintenanceDto.carMaintenance == null)
                return null;

            var prediction = predictionEngine.Predict(carMaintenanceDto.carMaintenance);

            var maintenanceDto = new MaintenanceDto();
            maintenanceDto.vin = carMaintenanceDto.vin;
            maintenanceDto.prediction = prediction;
            return maintenanceDto;
        }

    }
}