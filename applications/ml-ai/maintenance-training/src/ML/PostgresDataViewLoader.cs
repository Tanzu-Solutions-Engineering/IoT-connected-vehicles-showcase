using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.ML;
using Microsoft.ML.Data;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.src.ML
{
    public class PostgresDataViewLoader
    {
        private readonly string connectionString;

        public PostgresDataViewLoader(string connectionString)
        {
            this.connectionString = connectionString;
        }

        public IDataView Load(MLContext mlContext)
        {
                DatabaseLoader loader = mlContext.Data.CreateDatabaseLoader<CarMaintenance>();

                string sqlCommand = @"SELECT 
                                        slno,
                                        vehicle_type,brand,
                                        model,
                                        engine_type,
                                        make_year,
                                        region,
                                        mileage_range,
                                        mileage,
                                        (CASE WHEN label=1 THEN true ELSE false END)::boolean as label 
                                    FROM cars.maintenance_training";

                var dbSource = new DatabaseSource(Npgsql.NpgsqlFactory.Instance, connectionString, sqlCommand);

                return loader.Load(dbSource);
        }
    }
}
