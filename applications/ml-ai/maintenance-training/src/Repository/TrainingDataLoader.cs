using System.Data;
using System.Threading.Tasks;
using Microsoft.Extensions.Hosting;
using System.Threading;
using System;
using Microsoft.Extensions.Logging;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.Repository
{
    public class TrainingDataLoader : IHostedService
    {
        private static readonly object TRAING_TABLE_NM = "cars.maintenance_training";
        
        private string insertStatement = @"
        INSERT INTO "+ TRAING_TABLE_NM + @" (slno,vehicle_type,brand,model,engine_type,make_year,region,mileage_range,mileage,oil_filter,engine_oil,washer_plug_drain,dust_and_pollen_filter,whell_alignment_and_balancing,air_clean_filter,fuel_filter,spark_plug,brake_fluid,brake_and_clutch_oil,transmission_fluid,brake_pads,clutch,coolant,""cost"",""label"") VALUES
	 (1.0,1.0,1.0,1.0,1.0,2020.0,1.0,10000.0,11400.0,1.0,1.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2566.0,1),
	 (2.0,1.0,1.0,1.0,1.0,2020.0,1.0,10000.0,11453.0,1.0,1.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2633.0,1),
	 (3.0,1.0,1.0,1.0,1.0,2017.0,1.0,10000.0,10706.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,1),
	 (4.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,1706.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (5.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,2706.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (6.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,3706.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (7.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,4706.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (8.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,5706.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (9.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,6706.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (10.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,7706.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0); " +

    "INSERT INTO " + TRAING_TABLE_NM + @" (slno,vehicle_type,brand,model,engine_type,make_year,region,mileage_range,mileage,oil_filter,engine_oil,washer_plug_drain,dust_and_pollen_filter,whell_alignment_and_balancing,air_clean_filter,fuel_filter,spark_plug,brake_fluid,brake_and_clutch_oil,transmission_fluid,brake_pads,clutch,coolant,""cost"",""label"") VALUES
	 (11.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,8706.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (13.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,9106.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (14.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,9206.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (15.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,9306.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (16.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,9406.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (17.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,9506.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (18.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,9606.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (19.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,9606.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (20.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,9806.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0),
	 (12.0,1.0,1.0,1.0,1.0,2017.0,1.0,1500.0,9006.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,0);
        "
        +
    "INSERT INTO " + TRAING_TABLE_NM + @" (slno,vehicle_type,brand,model,engine_type,make_year,region,mileage_range,mileage,oil_filter,engine_oil,washer_plug_drain,dust_and_pollen_filter,whell_alignment_and_balancing,air_clean_filter,fuel_filter,spark_plug,brake_fluid,brake_and_clutch_oil,transmission_fluid,brake_pads,clutch,coolant,""cost"",""label"") VALUES
	 (1.0,1.0,1.0,1.0,1.0,2020.0,1.0,10000.0,11400.0,1.0,1.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2566.0,1),
	 (2.0,1.0,1.0,1.0,1.0,2020.0,1.0,10000.0,11453.0,1.0,1.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2633.0,1),
	 (3.0,1.0,1.0,1.0,1.0,2017.0,1.0,10000.0,10706.0,1.0,1.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,2643.0,1);
        ";

        private IDbConnection connection;
        private readonly ILogger<TrainingDataLoader> logger;
        

        public TrainingDataLoader(IDbConnection connection,ILogger<TrainingDataLoader> logger)
        {
            this.connection = connection;
            this.logger = logger;
        }

        public Task StartAsync(CancellationToken cancellationToken)
        {
            return Task.Run(LoadData);
        }

        public Task StopAsync(CancellationToken cancellationToken)
        {
            this.connection.Dispose();
            return Task.CompletedTask;
        }

        internal void LoadData()
        {
            connection.Open();
            int cnt = 0;

            using (var cmd = connection.CreateCommand())
            {
                cmd.CommandText = $"select count(*) as cnt from {TRAING_TABLE_NM}";
                using(var cntReader = cmd.ExecuteReader())
                {
                    if(cntReader.Read())
                         cnt = cntReader.GetInt32(0);
                }

                logger.LogInformation($"Table {TRAING_TABLE_NM} COUNT: {cnt}");

                if(cnt != 0)
                    return;

                cmd.CommandText = insertStatement;
                cmd.ExecuteNonQuery();
            };
        }
    }
}