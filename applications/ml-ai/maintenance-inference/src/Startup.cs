using Imani.Solutions.Core.API.Util;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.OpenApi.Models;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Prediction;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Inference.Stream.Rabbit;
using Steeltoe.Connector.PostgreSql;
using Steeltoe.Connector.RabbitMQ;
using Steeltoe.Management.Endpoint;
using Steeltoe.Messaging.Converter;
using Steeltoe.Messaging.RabbitMQ.Connection;
using Steeltoe.Messaging.RabbitMQ.Core;

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
            services.AddSingleton<IMessageConverter,NewtonJsonMessageConverter>();
            
            services.AddLogging(config =>
            {
                    // config.AddDebug();
                    config.AddConsole();
                    //etc
            });

            services.AddPostgresConnection(Configuration);
            services.AddRabbitMQConnection(Configuration);
            

            var predictor = new MaintenancePredictor();
            services.AddSingleton<IPredictor>(predictor);

            var updateModelConsumer = new UpdateModelConsumer(predictor);
            services.AddSingleton<UpdateModelConsumer>(updateModelConsumer);

            
            services.AddSingleton<ISettings>(new ConfigSettings());
            
              var connectionFactory = new CachingConnectionFactory()
                        {
                            Host = "localhost"
                        };
                var rabbitTemplate = new RabbitTemplate(connectionFactory);
                rabbitTemplate.MessageConverter = new NewtonJsonMessageConverter();
                services.AddSingleton<IRabbitTemplate>(rabbitTemplate);
                
            services.AddSingleton<RabbitStreamBuilder>(new RabbitStreamBuilder(updateModelConsumer,configSettings));


            services.AddAllActuators(Configuration);
            services.ActivateActuatorEndpoints();
            services.AddControllers();        

            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "predictive_maintenance", Version = "v1" });
            });

     
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
            app.UseRouting();
            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });

        }
    }
}
