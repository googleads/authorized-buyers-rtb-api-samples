/* Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

using Mono.Options;

using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;

namespace Google.Apis.RealTimeBidding.Examples
{
    internal class Program
    {
        /// <summary>
        /// A map to hold the code examples available to be executed.
        /// </summary>
        private static Dictionary<string, ExampleBase> examples =
            new Dictionary<string, ExampleBase>();

        /// <summary>
        /// Parse application arguments.
        /// </summary>
        private static void ParseArguments(string[] args, out string examplePath,
                                    out List<string> extras)
        {
            string ex = null;
            bool? showHelp = null;
            string exeName = Path.GetFileName(Assembly.GetExecutingAssembly().Location);

            OptionSet options = new OptionSet
            {
                "Runs Authorized Buyers Real-time Bidding API examples.",
                "",
                string.Format("Usage: dotnet run --example=path.to.Example [--example_arg1= ...]",
                              exeName),
                {
                    "h|help",
                    "Show help message and exit.",
                    h => showHelp = h != null
                },
                {
                    "example=",
                    "[Required] Path of the example that is intended to be run.",
                    example => ex = example
                },
            };

            extras = options.Parse(args);
            examplePath = ex;

            // Verify required arguments were set.
            if(examplePath == null)
            {
                // Show root-level help message.
                options.WriteOptionDescriptions(Console.Error);

                if(showHelp != true)
                {
                    Console.Error.WriteLine(@"\nRequired argument ""example"" not specified.");
                    Environment.Exit(1);
                }

                Environment.Exit(0);
            }
            else if(!examples.ContainsKey(examplePath))
            {
                Console.Error.WriteLine("Invalid example specified. It can be set to any of the " +
                                        "following:");
                foreach (KeyValuePair<string, ExampleBase> pair in examples)
                {
                    Console.Error.WriteLine("{0} : {1}", pair.Key, pair.Value.Description);
                }
                Environment.Exit(1);
            }
            else
            {
                // Pass along argument to show example-level help message.
                if(showHelp == true)
                {
                    extras.Add("--help");
                }
            }
        }


        /// <summary>
        /// Static constructor to initialize the examples map.
        /// </summary>
        static Program()
        {
            Type[] types = Assembly.GetExecutingAssembly().GetTypes();

            foreach (Type type in types)
            {
                if (type.IsSubclassOf(typeof(ExampleBase)))
                {
                    ExampleBase example = (ExampleBase)Activator.CreateInstance(type);
                    String exampleName = type.FullName.Replace(
                        typeof(Program).Namespace + ".", "");
                    examples.Add(exampleName, example);
                }
            }
        }

        /// <summary>
        /// The main method.
        /// </summary>
        /// <param name="args">Command line arguments - see ShowUsage for options</param>
        private static void Main(string[] args)
        {
            List<string> exampleArgs;
            string exampleName;
            ParseArguments(args, out exampleName, out exampleArgs);

            Console.WriteLine("Authorized Buyers Real-time Bidding API C# Sample");
            Console.WriteLine("====================");

            ExampleBase example = examples[exampleName];
            Console.WriteLine(example.Description);
            
            try
            {
                example.ExecuteExample(exampleArgs);
            }
            catch(ApplicationException ex)
            {
                Console.WriteLine(ex);
                Environment.Exit(1);
            }

            Console.WriteLine("Press any key to continue...");
            Console.ReadKey();
        }
    }
}
