using System.Reflection;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Domain;
using Microsoft.EntityFrameworkCore.Infrastructure;
using System.IO;
using System;


namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.src.Repository
{
    public class  CarMaintenanceDbContext : DbContext
    {
        private const string TESTING_EXE_NAME = "testhost.dll";
        private readonly string? schemaName;
        private readonly string? connectionString;

        public CarMaintenanceDbContext(IConfiguration config, DbContextOptions options) : base(options)
        {
            Console.WriteLine($"CONFIG={config}");
            this.schemaName = config["schemaName"];
            Console.WriteLine($"schemaName={schemaName}");
            this.connectionString = config.GetConnectionString("database");
        }

        protected override void OnModelCreating(ModelBuilder builder){

            builder.HasDefaultSchema(schemaName);    //To all the tables.

            builder.Entity<CarMaintenance>().ToTable("maintenance_training")
            .HasKey(cm => cm.slno).HasName("slno");

            builder.Entity<CarMaintenance>().Property(cm => cm.slno)
            .UseIdentityColumn();


            builder.ApplyConfigurationsFromAssembly(Assembly.GetExecutingAssembly());
        }

        protected override void OnConfiguring(DbContextOptionsBuilder options){
            
            var exeName = Path.GetFileName(Assembly.GetEntryAssembly().Location);

            if(TESTING_EXE_NAME.Equals(exeName))
            {
                //let Account.Data be null
                   options
                    .UseInMemoryDatabase("UserContextWithNullCheckingDisabled", 
                    b => b.EnableNullChecks(false));

                return; //do not Postgres connections
            }


            // options.UseNpgsql(connectionString,
            // x => x.MigrationsHistoryTable("__EFMigrationsHistory", schemaName));
        }

        public DbSet<CarMaintenance> CarMaintenances { get; set;}
        
    }
}