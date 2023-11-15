﻿using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Hosting;
using Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Stream;
using Steeltoe.Extensions.Configuration.ConfigServer;
using Steeltoe.Extensions.Configuration.Placeholder;
using Steeltoe.Extensions.Logging;
using Steeltoe.Stream.Extensions;

namespace Showcase.IoT.Connected.Vehicles.Predictive.Maintenance.Training
{
    public class Program
    {
        public static void Main(string[] args)
        {
            CreateHostBuilder(args).Build().Run();
        }

        public static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args)
                .UseDefaultServiceProvider(options =>
                    options.ValidateScopes = false)
                .ConfigureLogging((context, builder) => builder.AddDynamicConsole())
                 .ConfigureWebHostDefaults(webBuilder => 
                { 
                    webBuilder.UseStartup<Startup>(); 
                    webBuilder.AddPlaceholderResolver();
                })
                .AddDynamicLogging()
                .AddStreamServices<ModelBuilderProcessor>()
                .AddConfigServer();
    }
}
