using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ML;
using Polly;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.ML;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.ML
{
    public class MicrosoftMlTrainer : ITrainer
    {
        private LoadDataView dvLoader;
        private string outColumn = "Features";

        public MicrosoftMlTrainer(LoadDataView dvLoader)
        {
            this.dvLoader = dvLoader;
        }

        public byte[] Train()
        {

            var mlContext = new MLContext();
            var trainingDataView = dvLoader(mlContext);


             var pipeline = mlContext.Transforms.Concatenate(
                outColumn,
                "vehicle_type",
                "brand",
                "model",
                "engine_type",
                "make_year",
                "region",
                "mileage_range",
                "mileage"
                )
                .Append(mlContext.BinaryClassification.Trainers.FastTree(
                   labelColumnName: "label", featureColumnName: outColumn));

            ITransformer trainedModel = pipeline.Fit(trainingDataView);

            

            // var pipeline = mlContext.Transforms.Conversion.MapValueToKey(inputColumnName: "maintenance", outputColumnName: outColumn)
            //         .Append(mlContext.Transforms.Concatenate(outColumn,
            //         "vehicle_type", 
            //         "brand",
            //         "model",
            //         "engine_type",
            //         "make_year",
            //         "region",
            //         "mileage_range",
            //         "mileage"
            // //         ));

            // ITransformer trainedModel = pipeline.Fit(trainingDataView);

            using MemoryStream stream = new MemoryStream();
            {
                mlContext.Model.Save(trainedModel, trainingDataView.Schema, stream);
                byte[] outputBinary = stream.ToArray();

                return outputBinary;
            }
        }
    }
}