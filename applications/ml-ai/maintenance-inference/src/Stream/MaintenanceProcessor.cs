using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Extensions.Logging;
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
        private readonly ITransformer trainedModel;
        private PredictionEngine<CarMaintenance, MaintenancePrediction> predictionEngine;
        private readonly MLContext mlContext;

        public MaintenanceProcessor(ITransformer trainedModel)
        {
            this.mlContext = new MLContext();

            // Load trained model
           this.trainedModel = trainedModel;

           //Predict
         predictionEngine = mlContext.Model.CreatePredictionEngine<CarMaintenance, MaintenancePrediction>(trainedModel);
        }

        [StreamListener(IProcessor.INPUT)]
            [SendTo(IProcessor.OUTPUT)]
            public MaintenanceDto Predict(CarMaintenanceDto carMaintenanceDto)
            {
                 var prediction = predictionEngine.Predict(carMaintenanceDto.carMaintenance);

                 var maintenanceDto = new MaintenanceDto();
                 maintenanceDto.vin = carMaintenanceDto.vin;
                 maintenanceDto.prediction = prediction;
                return maintenanceDto;
            }
        
    }
}