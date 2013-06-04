function init(dom, Button, Select, aspect, registry, JSON, query, Memory, Observable, Tree, ObjectStoreModel, request, when, OnDemandGrid, arrayUtil,
    Menu, MenuItem, PopupMenuItem) {
    
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
        tree.startup();

        var gareResults = treeStore.query(function(object) {
            return (object.parent === "_Gares");
        });

        var gareStore = new Memory({
            data: gareResults,
            getChildren: function(object){
                return this.query({parent: object.id});
            }
        });

        var arcResults = treeStore.query(function(object) {
            return (object.parent === "_Arcs");
        });

        var arcStore = new Memory({
            data: arcResults,
            getChildren: function(object){
                return this.query({parent: object.id});
            }
        });

        var trajetResults = treeStore.query(function(object) {
            return (object.parent === "_Trajets");
        });

        var trajetStore = new Memory({
            data: trajetResults,
            getChildren: function(object){
                return this.query({parent: object.id});
            }
        });

        /*arrayUtil.forEach(jsonObj.wrapperDTO, function(item, index) {
            if (item.parent === "_Gares" || item.name === "Gares") {
                gareStore.add(item);
            }
            if (item.parent === "_Arcs" || item.name === "Arcs") {
                arcStore.add(item);
            }
            if (item.parent === "_Trajets" || item.name === "Trajets") {
                trajetStore.add(item);
            }
        });*/

        var gareGrid = new OnDemandGrid({
            store: gareStore,
            columns: jsonObj.gridElements.gare
        }, "gareGrid");
        gareGrid.startup();

        var arcGrid = new OnDemandGrid({
            store: arcStore,
            columns: jsonObj.gridElements.arc
        }, "arcGrid");
        arcGrid.startup();

        var trajetGrid = new OnDemandGrid({
            store: trajetStore,
            columns: jsonObj.gridElements.trajet
        }, "trajetGrid");
        trajetGrid.startup();

        tree.on("dblclick", function(object){
            //trajetGrid.set("showHeader", false);
            trajetGrid.refresh();
            //trajetGrid.set("showHeader", true);
            console.log("Hello");
            /*if (object.parent === "_Gares") {
                grid.store = gareStore;
                grid.columns = jsonObj.gridElements.gare;
                grid.refresh();
            }
            else if (object.parent === "_Arcs") {
                grid.store = arcStore;
                grid.columns = jsonObj.gridElements.arc;
                grid.refresh();
            }
            else if (object.parent === "_Trajets") {
                grid.store = trajetStore;
                grid.columns = jsonObj.gridElements.trajet;
                grid.refresh();
            }*/
        }, true);

        var gareDepSelect = new Select({
           name: "gareDepSelect",
           store: gareStore,
           style: "width: 200px; float: left;",
           labelAttr: "name",
           maxHeight: -1
        }, "gareDepartSelect");
        gareDepSelect.startup();

        var gareArrSelect = new Select({
           name: "gareArrSelect",
           store: gareStore,
           style: "width: 200px;",
           labelAttr: "name",
           maxHeight: -1
        }, "gareArriveeSelect");
        gareArrSelect.startup();

    }); 

    var button = new Button({
        iconClass:"dijitIconNewTask",
        showLabel:false,
        label: "Log out", // analogous to title when showLabel is false
        onClick: function(){
            location.assign("../../logout");
        }
    }, "logoutBtn");

    button.startup();

    var button2 = new Button({
        label: "Valider",
        onClick: function(){
            request.post("plusCourtChemin", {
                data: JSON.stringify({ idGareDepart: registry.byId("gareDepartSelect").get("value"),
                    idGareArrivee: registry.byId("gareArriveeSelect").get("value")
                }),
                headers: {
                "Content-Type": "application/json"
                },
                handleAs: "json"
            }).then(function(data) {
                if (data.errorMessage === "") {
                    dom.byId('responseContainer').innerHTML = JSON.stringify(
                    data.plusCourtChemin, null, "\t");
                }
                else {
                    dom.byId('errorMessageContainer2').innerHTML = data.errorMessage;
                }
            },
                function(error) {
                    dom.byId('errorMessageContainer2').innerHTML = error;
                })  ;
        }
    }, "DjikstraBtn");

    button2.startup();
    
    // create task menu as context menu for task nodes.
    var treeMenu = new Menu({
            id: "treeMenu",
            targetNodeIds: ["divTree"],
            selector: ".dijitTreeNode"
    });
    
    // define the task menu items
    treeMenu.addChild( new MenuItem({
            id: "begin",
            label: "Begin",
            onClick: function() {
                var tn = dijit.byNode(this.getParent().currentTarget);

                // now print the data store item that backs the tree node
                console.debug("menu click for item: ", tn.item.name);
            }
    }) );
    
    
    
    treeMenu.addChild( new MenuItem({
            id: "complete",
            label: "Mark as Complete",
            onClick: function() {
        
            }
    }) );
    
    treeMenu.startup();
    
}