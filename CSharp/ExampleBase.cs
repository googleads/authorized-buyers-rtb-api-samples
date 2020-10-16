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

using System.Collections.Generic;

namespace Google.Apis.RealTimeBidding.Examples
{
    /// <summary>
    /// This abstract class represents a code example.
    /// </summary>
    public abstract class ExampleBase
    {
        /// <summary>
        /// Returns a description about the code example.
        /// </summary>
        public abstract string Description
        {
            get;
        }

       /// <summary>
       /// Parse arguments and run the example.
       /// </summary>
       public void ExecuteExample(List<string> exampleArgs)
       {
           Dictionary<string, object> parsedArgs = ParseArguments(exampleArgs);
           Run(parsedArgs);
       }

        /// <summary>
        /// Parse specified arguments.
        /// </summary>
        protected abstract Dictionary<string, object> ParseArguments(List<string> exampleArgs);

        /// <summary>
        /// Run the example.
        /// </summary>
        protected abstract void Run(Dictionary<string, object> parsedArgs);
    }
}