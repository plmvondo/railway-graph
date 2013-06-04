<%-- 
    Document   : reseau
    Created on : 29 avr. 2013, 03:34:18
    Author     : Pierre
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="../../js/libs/dijit/themes/claro/claro.css" media="screen" />
        <link rel="stylesheet" href="../../css/style.css" media="screen" />
        <title>Railway network</title>
    </head>
    <body class="claro">
        <script>
            dojoConfig= {
                baseUrl: "../../js/",
                packages: [
                    { name: "dojo", location: "libs/dojo" },
                    { name: "dijit", location: "libs/dijit" },
                    { name: "dojox", location: "libs/dojox" },
                    { name: "util", location: "libs/util" },
                    { name: "dgrid", location: "libs/dgrid-master" },
                    { name: "put-selector", location: "libs/put-selector" },
                    { name: "xstyle", location: "libs/xstyle" }
                ],
                has: {
                    "dojo-firebug": false
                },
                async: true,
                locale: "fr-FR",
                isDebug: true
            };
        </script>
        <script src="../../js/libs/dojo/dojo.js"></script>
        <script>
            require(["dojo/parser", "dojo/ready", "dojo/dom", "dojo/dom-construct", "dojo/_base/lang", "dojo/on", "dojo/_base/fx", "dijit/form/Button", "dijit/form/Select", "dojo/aspect", "dijit/registry",
                "dojo/json", "dojo/query", "dojo/store/Memory", "dojo/store/Observable", "dijit/Tree", "dijit/tree/ObjectStoreModel", "dojo/request",
                "dojo/when", "dgrid/OnDemandGrid", "dojo/_base/array", "dijit/Menu", "dijit/MenuSeparator", "dijit/MenuItem", "dijit/PopupMenuItem", "dijit/focus", "dijit/form/CheckBox", "dijit/form/Form", "dijit/Dialog", "dijit/form/TextBox", "dijit/form/NumberTextBox", "dijit/form/ValidationTextBox", "dijit/form/Textarea", "dijit/layout/BorderContainer", "dijit/layout/TabContainer", "dijit/layout/ContentPane"],
                function(parser, ready, dom, domConstruct, lang, on, fx, Button, Select, aspect, registry, JSON, query, Memory, Observable, Tree, ObjectStoreModel, request, when, OnDemandGrid, arrayUtil, Menu, MenuSeparator, MenuItem, PopupMenuItem, focusUtil, CheckBox) {
                    ready(function() {
                        parser.parse(); 
                        //init(dom, Button, Select, aspect, registry, JSON, query, Memory, Observable, Tree, ObjectStoreModel, request, when, OnDemandGrid, arrayUtil, Menu, MenuItem, PopupMenuItem);
                        
                        var jsonObj;
                        var getPromise = function() {
                            var promise = request.post("treestore", {
                                handleAs: "json"
                            });
                            return promise.response.then(function(response) {
                                jsonObj = response.data;
                            });
                        };

                        when(getPromise(), function() {
                            // set up the store to get the tree data, plus define the method
                            // to query the children of a node
                            var treeStore = new Memory({
                                data: jsonObj.wrapperDTO,
                                getChildren: function(object){
                                    return this.query({parent: object.id});
                                },
                                getRoot: function() {
                                    return this.query({id: "root"});
                                }
                            });

                            // To support dynamic data changes, including DnD,
                            // the store must support put(child, {parent: parent}).
                            // But dojo/store/Memory doesn't, so we have to implement it.
                            // Since our store is relational, that just amounts to setting child.parent
                            // to the parent's id.
                            aspect.around(treeStore, "put", function(originalPut){
                                return function(obj, options){
                                    if(options && options.parent){
                                        obj.parent = options.parent.id;
                                    }
                                    return originalPut.call(treeStore, obj, options);
                                };
                            });

                            // give store Observable interface so Tree can track updates
                            treeStore = new Observable(treeStore);


                            var treeModel = new ObjectStoreModel({
                                store: treeStore,
                                query: {id: 'root'},
                                mayHaveChildren: function(object){
                                    // Normally the object would have some attribute (like a type) that would tell us if
                                    // it might have children.   But since our data is local we'll just check if there
                                    // actually are children or not.
                                    return this.store.getChildren(object).length > 0;
                                }
                            });

                            var tree = new Tree({
                                model: treeModel,
                                // define the drag-n-drop controller
                                //dndController: dndSource,
                                autoExpand: false
                            }, "divTree");
                            
                            // create task menu as context menu for task nodes.
                            var treeMenu = new Menu({
                                    id: "treeMenu",
                                    targetNodeIds: ["divTree"],
                                    selector: ".dijitTreeNode"
                            });

                            // define the task menu items
                            treeMenu.addChild( new MenuItem({
                                    id: "edit",
                                    label: "Modifier",
                                    iconClass: "dijitIconEditTask",
                                    onClick: function() {
                                        var tn = dijit.byNode(this.getParent().currentTarget);
                                        
                                        // now print the data store item that backs the tree node
                                        console.debug("menu click for item: ", tn.item.name);
                                        if (tn.item.parent === "_Stations") {
                                            registry.byId("created").set("value", tn.item.created);
                                            registry.byId("dbIdentifier").set("value", tn.item.id);
                                            registry.byId("editStationName").set("value", tn.item.name);
                                            registry.byId("editStation").show();
                                        }
                                        else if (tn.item.parent === "_Sections") {
                                            registry.byId("sectionCreated").set("value", tn.item.created);
                                            registry.byId("sectionDbIdentifier").set("value", tn.item.id);
                                            registry.byId("editSectionNbkms").set("value", tn.item.nbKms);
                                            registry.byId("editSectionDepartureStation").set("value", tn.item.departureStation);
                                            registry.byId("editSectionArrivalStation").set("value", tn.item.arrivalStation);
                                            registry.byId("editSectionName").set("value", tn.item.name);
                                            registry.byId("editSection").show();
                                        }
                                        else if (tn.item.parent === "_Paths") {
                                            registry.byId("pathCreated").set("value", tn.item.created);
                                            registry.byId("pathDbIdentifier").set("value", tn.item.id);
                                            registry.byId("editPathNbkms").set("value", tn.item.nbKms);
                                            registry.byId("editPathName").set("value", tn.item.name);
                                            registry.byId("editPathSections").set("value", tn.item.sections);
                                            registry.byId("editPath").show();
                                        }
                                    }
                            }) );
                            
                            treeMenu.addChild( new MenuItem({
                                    id: "delete",
                                    label: "Supprimer",
                                    iconClass: "dijitIconDelete",
                                    onClick: function() {
                                        var tn = dijit.byNode(this.getParent().currentTarget);
                                   
                                        // now print the data store item that backs the tree node
                                        console.debug("menu click for item: ", tn.item.name);
                                        if (tn.item.parent === "_Stations") {
                                            dom.byId("deleteStationSpan").innerHTML = tn.item.name;
                                            dom.byId("deleteStationId").value = tn.item.id;
                                            registry.byId("deleteStation").show();
                                        }
                                        else if (tn.item.parent === "_Sections") {
                                            dom.byId("deleteSectionSpan").innerHTML = tn.item.name;
                                            dom.byId("deleteSectionId").value = tn.item.id;
                                            registry.byId("deleteSection").show();
                                        }
                                        else if (tn.item.parent === "_Paths") {
                                            dom.byId("deletePathSpan").innerHTML = tn.item.name;
                                            dom.byId("deletePathId").value = tn.item.id;
                                            registry.byId("deletePath").show();
                                        }
                                    }
                            }) );
                            
                            var getCt = function() {
                                return dijit.byNode(this.currentTarget);
                            };
                            
                            treeMenu.addChild(new MenuSeparator());
                            
                            var pSubMenu = new Menu();
                            pSubMenu.addChild(new MenuItem({
                                id: "stationContextMenu",
                                label: "a station",
                                onClick: function() {
                                        var f = lang.hitch(treeMenu, getCt);
                                        var tn = f();
                                        // now print the data store item that backs the tree node
                                        console.debug("menu click for item: ", tn.item.name);
                                 
                                        registry.byId("addStation").show();
                                    }
                            }));
                            pSubMenu.addChild(new MenuItem({
                                id: "sectionContextMenu",
                                label: "a section",
                                onClick: function() {
                                        var f = lang.hitch(treeMenu, getCt);
                                        var tn = f();
                                        // now print the data store item that backs the tree node
                                        console.debug("menu click for item: ", tn.item.name);
                                 
                                        registry.byId("addSection").show();
                                    }
                            }));
                            pSubMenu.addChild(new MenuItem({
                                id: "pathContextMenu",
                                label: "a path",
                                onClick: function() {
                                        var f = lang.hitch(treeMenu, getCt);
                                        var tn = f();
                                        // now print the data store item that backs the tree node
                                        console.debug("menu click for item: ", tn.item.name);
                                 
                                        registry.byId("addPath").show();
                                    }
                            }));
                            
                            // define the task menu items
                            treeMenu.addChild( new PopupMenuItem({
                                    id: "add",
                                    label: "Add...",
                                    iconClass: "dijitIconNewTask",
                                    popup: pSubMenu
                            }) );

                            
                            treeMenu.startup();
                            tree.startup();
                            treeMenu.on("focus", function() {
                                var f = lang.hitch(this, getCt);
                                var tn = f();
                                if (tn.item.name === "Stations" || tn.item.name === "Sections" || tn.item.name === "Paths") {
                                    registry.byId("add").set("disabled", false);
                                    registry.byId("edit").set("disabled", true);
                                    registry.byId("delete").set("disabled", true);
                                    
                                    if (tn.item.name === "Stations") {
                                        registry.byId("stationContextMenu").set("disabled", false);
                                        registry.byId("sectionContextMenu").set("disabled", true);
                                        registry.byId("pathContextMenu").set("disabled", true);
                                    }
                                    else if (tn.item.name === "Sections") {
                                        registry.byId("stationContextMenu").set("disabled", true);
                                        registry.byId("sectionContextMenu").set("disabled", false);
                                        registry.byId("pathContextMenu").set("disabled", true);
                                    }
                                    else if (tn.item.name === "Paths") {
                                        registry.byId("stationContextMenu").set("disabled", true);
                                        registry.byId("sectionContextMenu").set("disabled", true);
                                        registry.byId("pathContextMenu").set("disabled", false);
                                    }
                                }
                                else {
                                    registry.byId("add").set("disabled", true);
                                    registry.byId("edit").set("disabled", false);
                                    registry.byId("delete").set("disabled", false);
                                }
                                console.debug(tn.item.name);
                            });
                            
                                
                            // now listen for any changes
                            var observeHandler = function(widget, widgetType) {
                                this.observe(function(object, removedFrom, insertedInto) {
                                    if(removedFrom > -1){ // existing object removed
                                        //removeRow(removedFrom);
                                        if (widgetType === "SelectStationWidget") {
                                            widget.setStore(stationStore);
                                        }
                                        else if (widgetType === "SelectSectionWidget") {
                                            widget.setStore(sectionStore);
                                        }
                                        else {
                                            widget.refresh();
                                        }
                                    }
                                    if(insertedInto > -1){ // new or updated object inserted
                                        //insertRow(insertedInto, object);
                                        if (widgetType === "SelectStationWidget") {
                                            widget.setStore(stationStore);
                                        }
                                        else if (widgetType === "SelectSectionWidget") {
                                            widget.setStore(sectionStore);
                                        }
                                        else {
                                            widget.refresh();
                                        }
                                    }
                                }, true);
                            };
                            
                            
                            var stationResults = treeStore.query(function(object) {
                                return (object.parent === "_Stations");
                            });
                            
                            var stationStore = new Memory({
                                data: stationResults
                            });

                            var sectionResults = treeStore.query(function(object) {
                                return (object.parent === "_Sections");
                            });

                            var sectionStore = new Memory({
                                data: sectionResults
                            });

                            var pathResults = treeStore.query(function(object) {
                                return (object.parent === "_Paths");
                            });

                            var pathStore = new Memory({
                                data: pathResults
                            });
                            
                            var emptyStore = new Memory({
                                data: []
                            });
                            
                            
                            var stationGrid = new OnDemandGrid({
                                store: emptyStore,
                                columns: jsonObj.gridHeaderNames.railwayStation,
                                loadingMessage: "Loading data..."
                            }, "stationGrid");
                            
                            var stationGridUpdate = lang.hitch(stationResults, observeHandler, stationGrid, "");
                            stationGridUpdate();
                            stationGrid.startup();
                            
                            
                            var sectionGrid = new OnDemandGrid({
                                store: emptyStore,
                                columns: jsonObj.gridHeaderNames.section,
                                loadingMessage: "Loading data..."
                            }, "sectionGrid");
                            var sectionGridUpdate = lang.hitch(sectionResults, observeHandler, sectionGrid, "");
                            sectionGridUpdate();
                            sectionGrid.startup();

                            var pathGrid = new OnDemandGrid({
                                store: emptyStore,
                                columns: jsonObj.gridHeaderNames.path,
                                loadingMessage: "Loading data..."
                            }, "pathGrid");
                            var pathGridUpdate = lang.hitch(pathResults, observeHandler, pathGrid, "");
                            pathGridUpdate();
                            pathGrid.startup();
                            
                            tree.on("dblclick", function(item){
                                var myStore, results;
                                
                                if (item.name === "Stations") {
                                    stationGrid.set("store", stationStore);
                                    registry.byId("graphTabCtn").selectChild(registry.byId("stationContentPane"));
                                }
                                else if (item.parent === "_Stations") {
                                    results = treeStore.query(function(object) {
                                        return (object.id === item.id);
                                    });

                                    myStore = new Memory({
                                        data: results
                                    });
                                    var onTreeDblClickUpdate1 = lang.hitch(results, observeHandler, stationGrid, "");
                                    onTreeDblClickUpdate1();
                                    stationGrid.set("store", myStore);
                                    registry.byId("graphTabCtn").selectChild(registry.byId("stationContentPane"));
                                }
                                else if (item.name === "Sections") {
                                    sectionGrid.set("store", sectionStore);
                                    registry.byId("graphTabCtn").selectChild(registry.byId("sectionContentPane"));
                                }
                                else if (item.parent === "_Sections") {
                                    results = treeStore.query(function(object) {
                                        return (object.id === item.id);
                                    });

                                    myStore = new Memory({
                                        data: results
                                    });
                                    var onTreeDblClickUpdate2 = lang.hitch(results, observeHandler, sectionGrid, "");
                                    onTreeDblClickUpdate2();
                                    sectionGrid.set("store", myStore);
                                    registry.byId("graphTabCtn").selectChild(registry.byId("sectionContentPane"));
                                }
                                else if (item.name === "Paths") {
                                    pathGrid.set("store", pathStore);
                                    registry.byId("graphTabCtn").selectChild(registry.byId("pathContentPane"));
                                }
                                else if (item.parent === "_Paths") {
                                    results = treeStore.query(function(object) {
                                        return (object.id === item.id);
                                    });

                                    myStore = new Memory({
                                        data: results
                                    });
                                    var onTreeDblClickUpdate3 = lang.hitch(results, observeHandler, pathGrid, "");
                                    onTreeDblClickUpdate3();
                                    pathGrid.set("store", myStore);
                                    registry.byId("graphTabCtn").selectChild(registry.byId("pathContentPane"));
                                }
                            }, true);
                            
                            // Show the dialog
                            var showEditStationDialog = function() {
                                var f = registry.byId("editStationForm");
                                on(f, "submit", function(evt) {
                                    evt.stopPropagation();
                                    evt.preventDefault();
                                    
                                if(!f.isValid()) {
                                    return false;
                                }
                                
                                request.post("editRailwayStation", {
                                        data: JSON.stringify({ railwayStationId: registry.byId("dbIdentifier").get("value"),
                                                railwayStationName: registry.byId("editStationName").get("value")}),
                                        headers: {
                                            "Content-Type": "application/json"
                                        },
                                        handleAs: "json"
                                    }).then(function(response) {
                                        if (response.errorMessage === "") {
                                            var result = treeStore.query({id: "_Stations"});
                                            treeStore.put(response.railwayStationDTO, {parent: result[0], overwrite: true});
                                        }
                                        else {
                                            dom.byId("errorMessageDialogSpan").innerHTML = response.errorMessage;
                                            registry.byId("errorMessageDialog").show();
                                        }
                                    });
                                    hideDialog('editStation');
                                });
                            };
                            showEditStationDialog();
                            
                            // Show the dialog
                            var showAddStationDialog = function() {
                                var f = registry.byId("addStationForm");
                                on(f, "submit", function(evt) {
                                    evt.stopPropagation();
                                    evt.preventDefault();
                                    
                                    if(!f.isValid()) {
                                        return false;
                                    }
                                    request.post("addRailwayStation", {
                                        data: JSON.stringify({ railwayStationName: registry.byId("addStationName").get("value"),
                                                railwayNetworkName: treeStore.getRoot()[0].name }),
                                        headers: {
                                            "Content-Type": "application/json"
                                        },
                                        handleAs: "json"
                                    }).then(function(response) {
                                        if (response.errorMessage === "") {
                                            var result = treeStore.query({id: "_Stations"});
                                            treeStore.put(response.railwayStationDTO, {parent: result[0], overwrite: false});
                                        }
                                        else {
                                            dom.byId("errorMessageDialogSpan").innerHTML = response.errorMessage;
                                            registry.byId("errorMessageDialog").show();
                                        }
                                    });
                                    hideDialog('addStation');
                                });
                            };
                            showAddStationDialog();
                            
                            // Show the dialog
                            var deleteStationDialog = function() {
                                    
                                request.post("deleteRailwayStation", {
                                    data: JSON.stringify({ railwayStationId: dom.byId("deleteStationId").value,
                                            railwayNetwork: treeStore.getRoot()[0].name }),
                                    headers: {
                                        "Content-Type": "application/json"
                                    },
                                    handleAs: "json"
                                }).then(function(response) {
                                    if (response.errorMessage === "") {
                                        treeStore.remove(response.railwayStationId);
                                    }
                                    else {
                                        dom.byId("errorMessageDialogSpan").innerHTML = response.errorMessage;
                                        registry.byId("errorMessageDialog").show();
                                    }
                                });
                                hideDialog('deleteStation');
                            };
                            
                            var deleteGBtn = registry.byId("deleteStationBtn");
                            on(deleteGBtn, "click", function() {
                                deleteStationDialog();
                            });
                            
                            // Show the dialog
                            var showEditSectionDialog = function() {
                                var f = registry.byId("editSectionForm");
                                on(f, "submit", function(evt) {
                                    evt.stopPropagation();
                                    evt.preventDefault();
                                    
                                if(!f.isValid()) {
                                    return false;
                                }
                                
                                request.post("editSection", {
                                        data: JSON.stringify({ sectionId: registry.byId("sectionDbIdentifier").get("value"),
                                                sectionName: registry.byId("editSectionName").get("value")}),
                                        headers: {
                                            "Content-Type": "application/json"
                                        },
                                        handleAs: "json"
                                    }).then(function(response) {
                                        if (response.errorMessage === "") {
                                            var result = treeStore.query({id: "_Sections"});
                                            treeStore.put(response.sectionDTO, {parent: result[0], overwrite: true});
                                        }
                                        else {
                                            dom.byId("errorMessageDialogSpan").innerHTML = response.errorMessage;
                                            registry.byId("errorMessageDialog").show();
                                        }
                                    });
                                    hideDialog('editSection');
                                });
                            };
                            showEditSectionDialog();
                            
                            // Show the dialog
                            var showAddSectionDialog = function() {
                                var f = registry.byId("addSectionForm");
                                on(f, "submit", function(evt) {
                                    evt.stopPropagation();
                                    evt.preventDefault();
                                    
                                    if(!f.isValid()) {
                                        return false;
                                    }
                                    
                                    request.post("addSection", {
                                        data: JSON.stringify({ sectionName: registry.byId("addSectionName").get("value"),
                                                departureStationId: registry.byId("addSectionDepartureStation").get("value"),
                                                arrivalStationId: registry.byId("addSectionArrivalStation").get("value"),
                                                sectionNbkms: registry.byId("addSectionNbkms").get("value"),
                                                railwayNetworkName: treeStore.getRoot()[0].name }),
                                        headers: {
                                            "Content-Type": "application/json"
                                        },
                                        handleAs: "json"
                                    }).then(function(response) {
                                        if (response.errorMessage === "") {
                                            var result = treeStore.query({id: "_Sections"});
                                            treeStore.put(response.sectionDTO, {parent: result[0], overwrite: false});
                                        }
                                        else {
                                            dom.byId("errorMessageDialogSpan").innerHTML = response.errorMessage;
                                            registry.byId("errorMessageDialog").show();
                                        }
                                    });
                                    hideDialog('addSection');
                                });
                            };
                            showAddSectionDialog();
                            
                            // Show the dialog
                            var deleteSectionDialog = function() {
                                    
                                request.post("deleteSection", {
                                    data: JSON.stringify({ sectionId: dom.byId("deleteSectionId").value,
                                            railwayNetworkName: treeStore.getRoot()[0].name }),
                                    headers: {
                                        "Content-Type": "application/json"
                                    },
                                    handleAs: "json"
                                }).then(function(response) {
                                    if (response.errorMessage === "") {
                                        treeStore.remove(response.sectionId);
                                    }
                                    else {
                                        dom.byId("errorMessageDialogSpan").innerHTML = response.errorMessage;
                                        registry.byId("errorMessageDialog").show();
                                    }
                                });
                                hideDialog('deleteSection');
                            };
                            
                            var deleteGBtn2 = registry.byId("deleteSectionBtn");
                            on(deleteGBtn2, "click", function() {
                                deleteSectionDialog();
                            });
                            
                            // Show the dialog
                            var showAddPathDialog = function() {
                                var f = registry.byId("addPathForm");
                                on(f, "submit", function(evt) {
                                    evt.stopPropagation();
                                    evt.preventDefault();
                                    
                                    if(!f.isValid()) {
                                        return false;
                                    }
                                    
                                    var array = [];
                                    var content = query(".dijitDialogPaneContentArea > div", f.domNode)[0];
                                    var nodes = query(".myTemplateClass", content);
                                    
                                    arrayUtil.forEach(nodes, function(item, index) {
                                        var i = index + 1;
                                        array.push({
                                            pathSectionId: registry.byId("pathSection" + i).get("value"),
                                            departureStationServed: registry.byId("checkboxDepartureSection" + i).get("value"),
                                            arrivalStationServed: registry.byId("checkboxArrivalSection" + i).get("value")
                                        });
                                    });
                            
                                    request.post("addPath", {
                                        data: JSON.stringify({ pathName: registry.byId("addPathName").get("value"),
                                                departureStationId: registry.byId("addPathDepartureStation").get("value"),
                                                arrivalStationId: registry.byId("addPathArrivalStation").get("value"),
                                                sectionsArray: array,
                                                railwayNetworkName: treeStore.getRoot()[0].name }),
                                        headers: {
                                            "Content-Type": "application/json"
                                        },
                                        handleAs: "json"
                                    }).then(function(response) {
                                        if (response.errorMessage === "") {
                                            var result = treeStore.query({id: "_Paths"});
                                            treeStore.put(response.pathDTO, {parent: result[0], overwrite: false});
                                        }
                                        else {
                                            dom.byId("errorMessageDialogSpan").innerHTML = response.errorMessage;
                                            registry.byId("errorMessageDialog").show();
                                        }
                                    });
                                    hideDialog('addPath');
                                });
                            };
                            showAddPathDialog();
                            
                            // Show the dialog
                            var showEditPathDialog = function() {
                                var f = registry.byId("editPathForm");
                                on(f, "submit", function(evt) {
                                    evt.stopPropagation();
                                    evt.preventDefault();
                                    
                                if(!f.isValid()) {
                                    return false;
                                }
                                
                                request.post("editPath", {
                                        data: JSON.stringify({ pathId: registry.byId("pathDbIdentifier").get("value"),
                                                pathName: registry.byId("editPathName").get("value")}),
                                        headers: {
                                            "Content-Type": "application/json"
                                        },
                                        handleAs: "json"
                                    }).then(function(response) {
                                        if (response.errorMessage === "") {
                                            var result = treeStore.query({id: "_Paths"});
                                            treeStore.put(response.pathDTO, {parent: result[0], overwrite: true});
                                        }
                                        else {
                                            dom.byId("errorMessageDialogSpan").innerHTML = response.errorMessage;
                                            registry.byId("errorMessageDialog").show();
                                        }
                                    });
                                    hideDialog('editPath');
                                });
                            };
                            showEditPathDialog();
                            
                            // Show the dialog
                            var deletePathDialog = function() {
                                    
                                request.post("deletePath", {
                                    data: JSON.stringify({ pathId: dom.byId("deletePathId").value,
                                            railwayNetworkName: treeStore.getRoot()[0].name }),
                                    headers: {
                                        "Content-Type": "application/json"
                                    },
                                    handleAs: "json"
                                }).then(function(response) {
                                    if (response.errorMessage === "") {
                                        treeStore.remove(response.pathId);
                                    }
                                    else {
                                        dom.byId("errorMessageDialogSpan").innerHTML = response.errorMessage;
                                        registry.byId("errorMessageDialog").show();
                                    }
                                });
                                hideDialog('deletePath');
                            };
                            
                            var deleteTBtn2 = registry.byId("deletePathBtn");
                            on(deleteTBtn2, "click", function() {
                                deletePathDialog();
                            });
                            
                            
                            var tabCtn = registry.byId("graphTabCtn");
                            tabCtn.watch("selectedChildWidget", function(name, oval, nval) {
                                var grid = nval.containerNode.firstElementChild;
                                if (grid.id === "pathGrid") {
                                    pathGrid.set("showHeader", false);
                                    pathGrid.set("showHeader", true);
                                }
                                if (grid.id === "sectionGrid") {
                                    sectionGrid.set("showHeader", false);
                                    sectionGrid.set("showHeader", true);
                                }
                                if (grid.id === "stationGrid") {
                                    stationGrid.set("showHeader", false);
                                    stationGrid.set("showHeader", true);
                                }
                            });
                            
                            var onFocusClear = function(dialogId, formTextBoxIds) {
                                var dialogWidget = registry.byId(dialogId);
                                on(dialogWidget, "show", function() {
                                    arrayUtil.forEach(formTextBoxIds, function(itemId, index) {
                                        registry.byId(itemId).reset();
                                    });
                                    if (dialogId === "addSection") {
                                        registry.byId("addSectionDepartureStation").reset();
                                        registry.byId("addSectionArrivalStation").reset();
                                    }
                                    else if (dialogId === "addPath") {
                                        registry.byId("addPathDepartureStation").reset();
                                        registry.byId("addPathArrivalStation").reset();
                                        registry.byId("pathSection1").reset();
                                        registry.byId("checkboxDepartureSection1").reset();
                                        registry.byId("checkboxArrivalSection1").reset();
                                        
                                        var f = registry.byId("addPathForm");
                                        var content = query(".dijitDialogPaneContentArea > div", f.domNode)[0];
                                        var nodes = query(".myTemplateClass", content);
                                        while (nodes.length > 1) {
                                            arrayUtil.forEach(registry.findWidgets(nodes[nodes.length - 1]), function(item, index) {
                                                item.destroyRecursive();
                                            });
                                            domConstruct.destroy(nodes[nodes.length - 1]);
                                            nodes.length--;
                                        }
                                    }
                                });
                            };
                            onFocusClear("addStation", ["addStationName"]);
                            onFocusClear("addSection", ["addSectionName", "addSectionNbkms"]);
                            onFocusClear("addPath", ["addPathName"]);
                            
                            var departureStationSelect = new Select({
                               name: "departureStationSelect",
                               store: stationStore,
                               style: "width: 200px; float: left;",
                               labelAttr: "name",
                               maxHeight: -1
                            }, "departureStationSelect");
                            var departureStationSelectUpdate = lang.hitch(stationResults, observeHandler, departureStationSelect, "SelectStationWidget");
                            departureStationSelectUpdate();
                            departureStationSelect.startup();

                            var arrivalStationSelect = new Select({
                               name: "arrivalStationSelect",
                               store: stationStore,
                               style: "width: 200px;",
                               labelAttr: "name",
                               maxHeight: -1
                            }, "arrivalStationSelect");
                            var arrivalStationSelectUpdate = lang.hitch(stationResults, observeHandler, arrivalStationSelect, "SelectStationWidget");
                            arrivalStationSelectUpdate();
                            arrivalStationSelect.startup();
                            
                            var addSectionDepartureStationSelect = new Select({
                               name: "addSectionDepartureStationSelect",
                               store: stationStore,
                               style: "width: 200px;",
                               labelAttr: "name",
                               maxHeight: -1,
                               required: true
                            }, "addSectionDepartureStation");
                            var addSectionDepartureStationUpdate = lang.hitch(stationResults, observeHandler, addSectionDepartureStationSelect, "SelectStationWidget");
                            addSectionDepartureStationUpdate();
                            addSectionDepartureStationSelect.startup();
                            
                            var addSectionArrivalStationSelect = new Select({
                               name: "addSectionArrivalStationSelect",
                               store: stationStore,
                               style: "width: 200px;",
                               labelAttr: "name",
                               maxHeight: -1,
                               required: true
                            }, "addSectionArrivalStation");
                            var addSectionArrivalStationUpdate = lang.hitch(stationResults, observeHandler, addSectionArrivalStationSelect, "SelectStationWidget");
                            addSectionArrivalStationUpdate();
                            addSectionArrivalStationSelect.startup();
                            
                            var addPathDepartureStationSelect = new Select({
                               name: "addPathDepartureStation",
                               store: stationStore,
                               style: "width: 200px;",
                               labelAttr: "name",
                               maxHeight: -1,
                               required: true
                            }, "addPathDepartureStation");
                            var addPathDepartureStationUpdate = lang.hitch(stationResults, observeHandler, addPathDepartureStationSelect, "SelectStationWidget");
                            addPathDepartureStationUpdate();
                            addPathDepartureStationSelect.startup();
                            
                            var addPathArrivalStationSelect = new Select({
                               name: "addPathArrivalStation",
                               store: stationStore,
                               style: "width: 200px;",
                               labelAttr: "name",
                               maxHeight: -1,
                               required: true
                            }, "addPathArrivalStation");
                            var addPathArrivalStationUpdate = lang.hitch(stationResults, observeHandler, addPathArrivalStationSelect, "SelectStationWidget");
                            addPathArrivalStationUpdate();
                            addPathArrivalStationSelect.startup();
                            
                            var addPathSection1Select = new Select({
                               name: "pathSection1",
                               store: sectionStore,
                               style: "width: 200px;",
                               labelAttr: "name",
                               maxHeight: -1,
                               required: true
                            }, "pathSection1");
                            var addPathSection1Update = lang.hitch(sectionResults, observeHandler, addPathSection1Select, "SelectSectionWidget");
                            addPathSection1Update();
                            addPathSection1Select.startup();
                            
                            registry.byId("addPathSectionBtn").on("click", function() {
                                var form = dom.byId("addPathForm");
                                var content = query(".dijitDialogPaneContentArea > div", form)[0];
                                var nodes = query(".myTemplateClass", content);
                                var i = nodes.length + 1;
                                var HTMLContent = "<br /><div class='leftFloat'><label for='pathSection" + i + "'>\n\
                                    Section " + i + ":</label><select id='pathSection" + i + "'></select>\n\
                                    </div><div><div class='leftFloat'><label for='checkboxDepartureSection" + i + "' class='addPathLabel'>\n\
                                    Departure:</label><input type='checkbox' id='checkboxDepartureSection" + i + "'></input>\n\
                                    </div><div><label for='checkboxArrivalSection" + i + "' class='addPathLabel'>Arrival:</label>\n\
                                    <input type='checkbox' id='checkboxArrivalSection" + i + "'></input></div></div>";
                                domConstruct.create("div", {
                                    id: "pathSectionContainer" + i,
                                    innerHTML: HTMLContent,
                                    className: "myTemplateClass"
                                }, content);
                                
                                var addPathSectionsSelect = new Select({
                                    name: "pathSection" + i,
                                    store: sectionStore,
                                    style: "width: 200px;",
                                    labelAttr: "name",
                                    maxHeight: -1,
                                    required: true
                                }, "pathSection" + i);
                                var addPathSectionsUpdate = lang.hitch(sectionResults, observeHandler, addPathSectionsSelect, "SelectSectionWidget");
                                addPathSectionsUpdate();
                                addPathSectionsSelect.startup();
                                
                                var checkbox1 = new CheckBox({
                                    name: "checkboxDepartureSection" + i,
                                    value: "true"
                                }, "checkboxDepartureSection" + i);
                                checkbox1.startup();
                                
                                var checkbox2 = new CheckBox({
                                    name: "checkboxArrivalSection" + i,
                                    value: "true"
                                }, "checkboxArrivalSection" + i);
                                checkbox2.startup();
                                
                            });
                            
                            registry.byId("undoPathSectionBtn").on("click", function() {
                                var form = dom.byId("addPathForm");
                                var content = query(".dijitDialogPaneContentArea > div", form)[0];
                                var nodes = query(".myTemplateClass", content);
                                if (nodes.length > 1) {
                                    arrayUtil.forEach(registry.findWidgets(nodes[nodes.length - 1]), function(item, index) {
                                        item.destroyRecursive();
                                    });
                                    domConstruct.destroy(nodes[nodes.length - 1]);
                                }
                            });
                        }); 
                        
                        hideDialog = function(id) {
                                registry.byId(id).hide();
                        };
                        
                        

                        var button = new Button({
                            iconClass:"dijitIconUsers",
                            showLabel:false,
                            label: "Log out",
                            onClick: function(){
                                location.assign("../../logout");
                            }
                        }, "logoutBtn");

                        button.startup();
                        
                        var loadingMessageInit = function(node) {
                            domConstruct.create("span", {
                               innerHTML: "Loading...",
                               style: {
                                   fontSize: "0.8em"
                               }
                            }, node);
                        };
                        
                        var loadingMessageDestroy = function(node) {
                            domConstruct.empty(node);
                        };
                        
                        var button2 = new Button({
                            label: "Send",
                            onClick: function(){
                                loadingMessageInit("DijkstraLoadingMessage");
                                request.post("shortestPath", {
                                    data: JSON.stringify({ departureStationId: registry.byId("departureStationSelect").get("value"),
                                        arrivalStationId: registry.byId("arrivalStationSelect").get("value")
                                    }),
                                    headers: {
                                    "Content-Type": "application/json"
                                    },
                                    handleAs: "json"
                                }).then(function(data) {
                                    if (data.errorMessage === "") {
                                        if (!data.shortestPath.length) {
                                            domConstruct.empty("DijkstraResponseContainer");
                                            domConstruct.create("span", {
                                                innerHTML: "There isn't a path between those two stations !",
                                                style: {
                                                    fontWeight: "bold",
                                                    color: "red"
                                                }  
                                            }, "DijkstraResponseContainer");
                                        }
                                        else {
                                            var totalDistance = 0.0;
                                            domConstruct.empty("DijkstraResponseContainer");
                                            domConstruct.create("ol", {
                                                id: "DijkstraList"
                                            }, "DijkstraResponseContainer");
                                            arrayUtil.forEach(data.shortestPath, function(item, index) {
                                                totalDistance += item.nbKms;
                                                domConstruct.create("li", {
                                                    innerHTML: item.name + "&nbsp;-------&nbsp;" + item.nbKms + "&nbsp;&nbsp;Kms",
                                                    style: {
                                                        fontStyle: "italic",
                                                        color: "blue"
                                                    }
                                                }, "DijkstraList");
                                            });
                                            domConstruct.create("span", {
                                                innerHTML: "Total distance:&nbsp;" + totalDistance + "&nbsp;Kms",
                                                style: {
                                                    fontWeight: "bold",
                                                    color: "red"
                                                }
                                            }, "DijkstraResponseContainer");
                                        } 
                                    }
                                    else {
                                        dom.byId("errorMessageDialogSpan").innerHTML = data.errorMessage;
                                        registry.byId("errorMessageDialog").show();
                                    }
                                    loadingMessageDestroy("DijkstraLoadingMessage");
                                },
                                    function(error) {
                                        loadingMessageDestroy("DijkstraLoadingMessage");
                                        dom.byId("errorMessageDialogSpan").innerHTML = error;
                                        registry.byId("errorMessageDialog").show();
                                    });
                            }
                        }, "DijkstraBtn");

                        button2.startup();
                       
                    });
                    
            });
        </script>
        
        <div id="appLayout" class="graphLayout" data-dojo-type="dijit/layout/BorderContainer"
            data-dojo-props="design: 'headline'">
            <div class="graphLayout" data-dojo-type="dijit/layout/BorderContainer" data-dojo-props="region: 'center', design: 'headline'">
                <div id="graphTabCtn" class="centerPanel" style="height: 40%" data-dojo-type="dijit/layout/TabContainer" data-dojo-props="region: 'center', tabPosition: 'top'">
                    <div id="stationContentPane" data-dojo-type="dijit/layout/ContentPane" data-dojo-props="title: 'Stations'">
                        <div id="stationGrid"></div>
                    </div>
                    <div id="sectionContentPane" data-dojo-type="dijit/layout/ContentPane" data-dojo-props="title: 'Sections', selected: 'true'">
                        <div id="sectionGrid"></div>
                    </div>
                    <div id="pathContentPane" data-dojo-type="dijit/layout/ContentPane" data-dojo-props="title: 'Paths'">
                        <div id="pathGrid"></div>
                    </div>
                </div>
                <div class="edgePanel" style="height: 40%" data-dojo-type="dijit/layout/TabContainer" data-dojo-props="region: 'bottom', splitter: true, tabPosition: 'top'">
                    <div data-dojo-type="dijit/layout/ContentPane" data-dojo-props="title: 'Find the shortest path'">
                        <div id="gdCtn" style="float: left">
                            <label for="departureStationSelect">Departure station:</label>
                            <select id="departureStationSelect" name="departureStationSelect"></select>
                        </div>
                        <div style="float: right">
                            <label for="arrivalStationSelect">Arrival station:</label>
                            <select id="arrivalStationSelect" name="arrivalStationSelect"></select>
                        </div>
                        <div id="DijkstraBtnContainer" style="clear: both; margin-top: 2em;">
                            <button id="DijkstraBtn" type="button"></button>
                        </div>
                        <div id="DijkstraLoadingMessage" style="height: auto; margin-left: 5em;"></div>
                        <div id="DijkstraResponseContainer"></div>
                    </div>
                </div>
            </div>
 
            <div id="topContainer" class="edgePanel" data-dojo-type="dijit/layout/ContentPane"
                data-dojo-props="region: 'top'">
                <div style="float: left; margin-left: 33%; margin-right: 33%;">RAILWAY NETWORK GRAPH</div>
                <s:if test="#session['user'] != null">
                    <s:property value="#session['user'].username" />
                </s:if>
                <div id="logoutBtnContainer">
                    <button id="logoutBtn" type="button">
                    </button>
                </div>
            </div>
            <div id="leftCol" class="edgePanel" data-dojo-type="dijit/layout/ContentPane"
                 data-dojo-props="region: 'left', splitter: true">
                <div id="divTree"></div>
            </div>
        </div>
        
        <div id="editStation" data-dojo-type="dijit/Dialog" data-dojo-props="title:'Edit a station'" style="display: none">
            <form data-dojo-type="dijit/form/Form" id="editStationForm">
                <div class="dijitDialogPaneContentArea">
                    <div style="overflow: auto; width:600px">
                        <label for="created">Creation date:</label>
                        <input id="created" type="text" name="created" disabled="true" data-dojo-type="dijit/form/TextBox" />
                        <br /><br />
                        <label for="dbIdentifier">Identifier:</label>
                        <input id="dbIdentifier" type="text" name="dbIdentifier" disabled="true" data-dojo-type="dijit/form/TextBox" />
                        <br /><br />
                        <label for="editStationName">Name:</label>
                        <input id="editStationName" type="text" name="editStationName" data-dojo-type="dijit/form/ValidationTextBox" required="true"
                              data-dojo-props="placeHolder:'Enter name here.', missingMessage:'This field is required.'" />
                    </div>
                </div>

                <div class="dijitDialogPaneActionBar">
                    <button data-dojo-type="dijit/form/Button" type="submit">OK</button>
                    <button data-dojo-type="dijit/form/Button" type="button" onclick="hideDialog('editStation');">Cancel</button>
                </div>
            </form>
        </div>

        <div id="addStation" class="graphDialog" data-dojo-type="dijit/Dialog" data-dojo-props="title:'Add a station'" style="display: none">
            <form data-dojo-type="dijit/form/Form" id="addStationForm">
                <div class="dijitDialogPaneContentArea">
                    <div style="overflow: auto; width:600px">
                        <label for="addStationName">Name:</label>
                        <input id="addStationName" type="text" name="addStationName" data-dojo-type="dijit/form/ValidationTextBox"
                              data-dojo-props="placeHolder:'Enter name here.', missingMessage:'This field is required.', required: 'true'" />
                    </div>
                </div>

                <div class="dijitDialogPaneActionBar">
                    <button data-dojo-type="dijit/form/Button" type="submit">OK</button>
                    <button data-dojo-type="dijit/form/Button" type="button" onclick="hideDialog('addStation');">Cancel</button>
                </div>
            </form>
        </div>
        
        <div id="deleteStation" data-dojo-type="dijit/Dialog" data-dojo-props="title:'Delete a station'" style="display: none">
            <div class="dijitDialogPaneContentArea">
                <input id="deleteStationId" type="hidden" value="" />
                Do you really want to delete&nbsp;<span id="deleteStationSpan"></span>&nbsp;?
            </div>

            <div class="dijitDialogPaneActionBar">
                <button id="deleteStationBtn" data-dojo-type="dijit/form/Button" type="button">OK</button>
                <button data-dojo-type="dijit/form/Button" type="button" onclick="hideDialog('deleteStation');">Cancel</button>
            </div>
        </div>
        
        <div id="editSection" data-dojo-type="dijit/Dialog" data-dojo-props="title:'Edit a section'" style="display: none">
            <form data-dojo-type="dijit/form/Form" id="editSectionForm">
                <div class="dijitDialogPaneContentArea">
                    <div style="overflow: auto; width:600px">
                        <label for="sectionCreated">Creation date:</label>
                        <input id="sectionCreated" type="text" name="sectionCreated" disabled="true" data-dojo-type="dijit/form/TextBox" />
                        <br /><br />
                        <label for="sectionDbIdentifier">Identifier:</label>
                        <input id="sectionDbIdentifier" type="text" name="sectionDbIdentifier" disabled="true" data-dojo-type="dijit/form/TextBox" />
                        <br /><br />
                        <label for="editSectionName">Name:</label>
                        <input id="editSectionName" type="text" name="editSectionName" data-dojo-type="dijit/form/ValidationTextBox"
                              data-dojo-props="placeHolder:'Enter name here.', missingMessage:'this field is required.', required: 'true'" />
                        <br /><br />
                        <label for="editSectionDepartureStation">Departure station:</label>
                        <input id="editSectionDepartureStation" type="text" name="editSectionDepartureStation" disabled="true" data-dojo-type="dijit/form/TextBox" />
                        <br /><br />
                        <label for="editSectionArrivalStation">Arrival station:</label>
                        <input id="editSectionArrivalStation" type="text" name="editSectionArrivalStation" disabled="true" data-dojo-type="dijit/form/TextBox" />
                        <br /><br />
                        <label for="editSectionNbkms">Nb of kms:</label>
                        <input id="editSectionNbkms" type="text" name="editSectionNbkms" disabled="true" data-dojo-type="dijit/form/ValidationTextBox"
                              data-dojo-props="placeHolder:'Enter kms here.', invalidMessage:'Only numbers are valid.', missingMessage:'This field is required.', required: 'true'" />
                    </div>
                </div>

                <div class="dijitDialogPaneActionBar">
                    <button data-dojo-type="dijit/form/Button" type="submit">OK</button>
                    <button data-dojo-type="dijit/form/Button" type="button" onclick="hideDialog('editSection');">Cancel</button>
                </div>
            </form>
        </div>
        
        <div id="addSection" class="graphDialog" data-dojo-type="dijit/Dialog" data-dojo-props="title:'Add a section'" style="display: none">
            <form data-dojo-type="dijit/form/Form" id="addSectionForm">
                <div class="dijitDialogPaneContentArea">
                    <div style="overflow: auto; width:600px">
                        <label for="addSectionName">Name:</label>
                        <input id="addSectionName" type="text" name="addSectionName" data-dojo-type="dijit/form/ValidationTextBox"
                              data-dojo-props="placeHolder:'Enter name here.', missingMessage:'This field is required.', required: 'true'" />
                        <br /><br />
                        <label for="addSectionDepartureStation">Departure station:</label>
                        <select id="addSectionDepartureStation" name="addSectionDepartureStation"></select>
                        <br /><br />
                        <label for="addSectionArrivalStation">Arrival station:</label>
                        <select id="addSectionArrivalStation" name="addSectionArrivalStation"></select>
                        <br /><br />
                        <label for="addSectionNbkms">Nb of kms:</label>
                        <input id="addSectionNbkms" type="text" name="addSectionNbkms" data-dojo-type="dijit/form/ValidationTextBox"
                              data-dojo-props="pattern: '\\d+\\.{1}\\d+', placeHolder:'Enter kms here.', invalidMessage:'Only floating-point numbers like 125.23.', missingMessage:'This field is required.', required: 'true'" />
                    </div>
                </div>

                <div class="dijitDialogPaneActionBar">
                    <button data-dojo-type="dijit/form/Button" type="submit">OK</button>
                    <button data-dojo-type="dijit/form/Button" type="button" onclick="hideDialog('addSection');">Cancel</button>
                </div>
            </form>
        </div>
        
        <div id="deleteSection" data-dojo-type="dijit/Dialog" data-dojo-props="title:'Delete a section'" style="display: none">
            <div class="dijitDialogPaneContentArea">
                <input id="deleteSectionId" type="hidden" value="" />
                Do you really want to delete&nbsp;<span id="deleteSectionSpan"></span>&nbsp;?
            </div>

            <div class="dijitDialogPaneActionBar">
                <button id="deleteSectionBtn" data-dojo-type="dijit/form/Button" type="button">OK</button>
                <button data-dojo-type="dijit/form/Button" type="button" onclick="hideDialog('deleteSection');">Cancel</button>
            </div>
        </div>
        
        <div id="addPath" class="graphDialog" data-dojo-type="dijit/Dialog" data-dojo-props="title:'Add a path'" style="display: none">
            <form data-dojo-type="dijit/form/Form" id="addPathForm">
                <div class="dijitDialogPaneContentArea">
                    <div style="overflow: auto; width:600px">
                        <label for="addPathName">Name:</label>
                        <input id="addPathName" type="text" name="addPathName" data-dojo-type="dijit/form/ValidationTextBox"
                              data-dojo-props="placeHolder:'Enter name here.', missingMessage:'This field is required.', required: 'true'" />
                        <br /><br />
                        <label for="addPathDepartureStation">Departure station:</label>
                        <select id="addPathDepartureStation" name="addPathDepartureStation"></select>
                        <br /><br />
                        <label for="addPathArrivalStation">Arrival station:</label>
                        <select id="addPathArrivalStation" name="addPathArrivalStation"></select>
                        <br /><br />
                        <div id="pathSectionContainer" class="myTemplateClass">
                            <div class="leftFloat">
                                <label for="pathSection1">Section 1:</label>
                                <select id="pathSection1" name="pathSection1"></select>
                            </div>
                            <div>
                                <div class="leftFloat">
                                    <input type="checkbox" id="checkboxDepartureSection1" name="checkboxDepartureSection1" data-dojo-type="dijit/form/CheckBox" value="true" />
                                    <label for="checkboxDepartureSection1" class="addPathLabel">Departure:</label>
                                </div>
                                <div>
                                    <label for="checkboxArrivalSection1" class="addPathLabel">Arrival:</label>
                                    <input type="checkbox" id="checkboxArrivalSection1" name="checkboxArrivalSection1" data-dojo-type="dijit/form/CheckBox" value="true"  />
                                </div> 
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="dijitDialogPaneActionBar">
                    <button id="addPathSectionBtn" data-dojo-type="dijit/form/Button" type="button">+</button>
                    <button id="undoPathSectionBtn" data-dojo-type="dijit/form/Button" type="button">-</button>
                    <button data-dojo-type="dijit/form/Button" type="submit">OK</button>
                    <button data-dojo-type="dijit/form/Button" type="button" onclick="hideDialog('addPath');">Cancel</button>
                </div>
            </form>
        </div>
                
        <div id="editPath" data-dojo-type="dijit/Dialog" data-dojo-props="title:'Edit a path'" style="display: none">
            <form data-dojo-type="dijit/form/Form" id="editPathForm">
                <div class="dijitDialogPaneContentArea">
                    <div style="overflow: auto; width:600px">
                        <label for="pathCreated">Creation date:</label>
                        <input id="pathCreated" type="text" name="pathCreated" disabled="true" data-dojo-type="dijit/form/TextBox" />
                        <br /><br />
                        <label for="pathDbIdentifier">Identifier:</label>
                        <input id="pathDbIdentifier" type="text" name="pathDbIdentifier" disabled="true" data-dojo-type="dijit/form/TextBox" />
                        <br /><br />
                        <label for="editPathName">Name:</label>
                        <input id="editPathName" type="text" name="editPathName" data-dojo-type="dijit/form/ValidationTextBox"
                              data-dojo-props="placeHolder:'Enter name here.', missingMessage:'This field is required.', required: 'true'" />
                        <br /><br />
                        <label for="editPathNbkms">Nb of kms:</label>
                        <input id="editPathNbkms" type="text" name="editPathNbkms" disabled="true" data-dojo-type="dijit/form/ValidationTextBox" />
                        <br /><br />
                        <label for="editPathSections">Sections list:</label>
                        <textarea id="editPathSections" cols="50" name="editPathSections" disabled="true" data-dojo-type="dijit/form/Textarea"></textarea>
                    </div>
                </div>

                <div class="dijitDialogPaneActionBar">
                    <button data-dojo-type="dijit/form/Button" type="submit">OK</button>
                    <button data-dojo-type="dijit/form/Button" type="button" onclick="hideDialog('editPath');">Cancel</button>
                </div>
            </form>
        </div>
        
        <div id="deletePath" data-dojo-type="dijit/Dialog" data-dojo-props="title:'Delete a path'" style="display: none">
            <div class="dijitDialogPaneContentArea">
                <input id="deletePathId" type="hidden" value="" />
                Do you really want to delete&nbsp;<span id="deletePathSpan"></span>&nbsp;?
            </div>

            <div class="dijitDialogPaneActionBar">
                <button id="deletePathBtn" data-dojo-type="dijit/form/Button" type="button">OK</button>
                <button data-dojo-type="dijit/form/Button" type="button" onclick="hideDialog('deletePath');">Cancel</button>
            </div>
        </div>
        
        <div id="errorMessageDialog" data-dojo-type="dijit/Dialog" data-dojo-props="title:'ERROR'" style="display: none">
            <div class="dijitDialogPaneContentArea">
                <div style="overflow: auto; width:500px">
                    <span id="errorMessageDialogSpan"></span>
                </div>
            </div>

            <div class="dijitDialogPaneActionBar">
                <button data-dojo-type="dijit/form/Button" type="button" onclick="hideDialog('errorMessageDialog');">OK</button>
            </div>
        </div>
    </body>
</html>
