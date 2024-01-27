using Imani.Solutions.Core.API.Util;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.OpenApi.Models;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.ML;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.src.ML;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.Repository;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training.src.Repository;
using Steeltoe.Connector.PostgreSql;
using Steeltoe.Connector.PostgreSql.EFCore;
using Steeltoe.Connector.RabbitMQ;
using Steeltoe.Management.Endpoint;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training
{
    public class Startup
    {
        private readonly ConfigSettings configSettings;

        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
            configSettings = new ConfigSettings();
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddDbContext<CarMaintenanceDbContext>(options => options.UseNpgsql(Configuration));
            services.AddPostgresConnection(Configuration);
            services.AddRabbitMQConnection(Configuration);
            services.AddAllActuators(Configuration);
            services.ActivateActuatorEndpoints();
            services.AddControllers();
            services.AddScoped<ICarMaintenanceRepository,CarMaintenanceRepository>();
            services.AddLogging(config =>
            {
                    config.AddDebug();
                    config.AddConsole();
                    //etc
            });

            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "predictive_maintenance", Version = "v1" });
            });

            services.AddHostedService<TrainingDataLoader>();

            //Building ML
            services.AddSingleton<ITrainer,MicrosoftMlTrainer>();

            services.AddSingleton<LoadDataView>(
                    new PostgresDataViewLoader(
                        configSettings.GetProperty("ConnectionString")
                        ).Load);

        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseSwagger();
                app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "predictive_maintenance"));
            }

            app.UseHttpsRedirection();
            app.UseDefaultFiles();
            app.UseStaticFiles();
            app.UseRouting();
            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}
