<%-- 
    Document   : login
    Created on : 21 avr. 2013, 21:55:45
    Author     : Pierre
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="css/style.css" media="screen" />
        <link rel="stylesheet" href="js/libs/dijit/themes/claro/claro.css" media="screen" />
        <title>Login Page</title>
    </head>
    <body class="claro">
        
        <script>
            dojoConfig= {
                baseUrl: "js/",
                packages: [
                    { name: "dojo", location: "libs/dojo" },
                    { name: "dijit", location: "libs/dijit" },
                    { name: "dojox", location: "libs/dojox" },
                    { name: "util", location: "libs/util" }
                ],
                has: {
                    "dojo-firebug": false
                },
                async: true,
                locale: "en_US",
                isDebug: true
            };
        </script>
        <script src="js/libs/dojo/dojo.js"></script>
        <script>
            require(["dojo/parser", "dojo/ready", "dojo/dom", "dojo/on", "dojo/when", "dojo/request", "dijit/registry", "dojo/json",
                "dijit/form/Form", "dijit/form/Button", "dijit/layout/BorderContainer",
                "dijit/layout/ContentPane", "dijit/form/ValidationTextBox"],
                function(parser, ready, dom, on, when, request, registry, JSON) {
                    ready(function() {
                        parser.parse();
                        var successfulPromise;
                        
                        var form = registry.byId('myForm');
                        on(form, "submit", function(evt) {
                            
                            evt.stopPropagation();
                            evt.preventDefault();
                            
                            if(!form.isValid()) {
                                return false;
                            }
                                
                            var getPromise = function() {
                                var promise = request.post("loginAuth?request_locale=en_US", {
                                // Send the username and password
                                data: JSON.stringify({ username: registry.byId("username").get("value"),
                                        password: registry.byId("password").get("value")}),
                                headers: {
                                    "Content-Type": "application/json"
                                },
                                handleAs: "json"
                                });

                                return promise.response.then(function(response){

                                // get the message from the data property
                                var message = response.data;
                                if (message.errorMessage === "") {
                                    successfulPromise = true;
                                }

                                dom.byId('errorMessageContainer').innerHTML = message.errorMessage;
                                },
                                function(error) {
                                    dom.byId('errorMessageContainer').innerHTML = error;
                                });
                            };

                            when(getPromise(), function() {
                                if (successfulPromise) {
                                    location.replace("services/json/railwayNetwork");
                                }
                            });
 
                        });
                    });            
                });
        </script>
        
        <div id="appLayout" data-dojo-type="dijit/layout/BorderContainer"
             data-dojo-props="design: 'headline'">
            <div class="edgePanel" data-dojo-type="dijit/layout/ContentPane"
                    data-dojo-props="region: 'center'">
                    
                <form data-dojo-type="dijit/form/Form" id="myForm">
                    <label for="username">Name:</label>
                    <input id="username" type="text" name="username" data-dojo-type="dijit/form/ValidationTextBox" required="true"
                           data-dojo-props="placeHolder:'Enter name here.', missingMessage:'Ce champ est requis.'" />
                    <br /><br />
                    <label for="password">Password:</label>
                    <input id="password" type="password" name="password" data-dojo-type="dijit/form/ValidationTextBox" required="true"
                           data-dojo-props="placeHolder:'Enter password here.', missingMessage:'Ce champ est requis.'" />
                    <br /><br />
                    <div id="btn1Container">
                        <button id="btn1" data-dojo-type="dijit/form/Button" type="submit">Valider</button>
                    </div>
                    <br />
                    <div id="errorMessageContainer" style="margin-left: 5em; color: red; font-weight: bold"></div>
                </form>
            </div>
        </div>
    </body>
</html>
