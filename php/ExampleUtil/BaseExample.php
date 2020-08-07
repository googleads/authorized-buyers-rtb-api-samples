<?php

/**
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

namespace Google\Ads\AuthorizedBuyers\RealTimeBidding\ExampleUtil;

use function HtmlHelper\printSampleHtmlFooter;

/**
 * Base class for all examples, containing helper methods to support examples
 * input and rendering results.
 */
abstract class BaseExample
{

    /**
     * Contains the logic of the example.
     */
    abstract protected function run();

    /**
     * Executes the example, checks if the example requires parameters and
     * request them before invoking run.
     */
    public function execute()
    {
        if (count($this->getInputParameters())) {
            if ($this->isSubmitComplete()) {
                $this->formValues = $this->getFormValues();
                $this->run();
            } else {
                $this->renderInputForm();
            }
        } else {
            $this->run();
        }
    }

    /**
     * Gives a display name of the example.
     * To be implemented in the specific example class.
     */
    abstract public function getName();

    /**
     * Returns the list of input parameters of the example.
     * To be overriden by examples that require parameters.
     * @return array
     */
    protected function getInputParameters()
    {
        return [];
    }

    /**
     * Renders an input form to capture the example parameters.
     */
    protected function renderInputForm()
    {
        $parameters = $this->getInputParameters();
        if (count($parameters)) {
            printf("<h2>Enter %s parameters</h2>", $this->getName());
            print '<form method="POST"><fieldset>';
            foreach ($parameters as $parameter) {
                $name = $parameter['name'];
                $display = $parameter['display'];
                $currentValue = isset($_POST[$name]) ? $_POST[$name] : '';
                print "<b>$display</b>: <input name='$name' value='$currentValue'>";
                if ($parameter['required']) {
                      print '*';
                }

                print '</br>';
                print "<small>$parameter[description]</small>";

                print '</br></br>';
            }
            print '</fieldset>*required<br/>';
            print '<input type="submit" name="submit" value="Submit"/>';
            print '</form>';
        }
    }

    /**
     * Checks if the form has been submitted and all required parameters are
     * set.
     * @return bool
     */
    protected function isSubmitComplete()
    {
        if (!isset($_POST['submit'])) {
            return false;
        }
        foreach ($this->getInputParameters() as $parameter) {
            if ($parameter['required'] && empty($_POST[$parameter['name']])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves the submitted form values.
     * @return array
     */
    protected function getFormValues()
    {
        $input = array();
        foreach ($this->getInputParameters() as $parameter) {
            if (!empty($_POST[$parameter['name']])) {
                if (isset($parameter['is_array']) and $parameter['is_array']) {
                    $input[$parameter['name']] = explode(',', $_POST[$parameter['name']]);
                } else {
                    $input[$parameter['name']] = $_POST[$parameter['name']];
                }
            } elseif (!empty($parameter['default'])) {
                $input[$parameter['name']] = $parameter['default'];
            } else {
                $input[$parameter['name']] = null;
            }
        }
        return $input;
    }

    /**
     * Prints out the given result object.
     * @param array $result
     */
    protected function printResult($result)
    {
        print '<pre>';
        print_r($result);
        print '</pre>';
    }
}
