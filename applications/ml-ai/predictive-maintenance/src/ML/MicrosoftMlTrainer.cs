using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ML;
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
                "mileage",
                "oil_filter",
                "engine_oil",
                "washer_plug_drain",
                "dust_and_pollen_filter",
                "whell_alignment_and_balancing",
                "air_clean_filter",
                "fuel_filter",
                "spark_plug",
                "brake_fluid",
                "brake_and_clutch_oil",
                "transmission_fluid",
                "brake_pads",
                "clutch",
                "coolant",
                "cost"
                )
                .Append(mlContext.BinaryClassification.Trainers.FastTree(
                    labelColumnName: "label", featureColumnName: outColumn));

            ITransformer trainedModel = pipeline.Fit(trainingDataView);


            //to Onnxx
             using MemoryStream stream = new MemoryStream();
            {
                mlContext.Model.ConvertToOnnx(trainedModel, trainingDataView, stream);
                byte[] outputBinary = stream.ToArray();

                return outputBinary;
            }
        }
    }
}