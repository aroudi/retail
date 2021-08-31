/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('incidentController', function($scope, $state, UserService, baseDataService, incidentService , SUCCESS, ALL_CATEGORY_OFTYPE_URI, ALL_SCRIPT_URI,
                            NOTIFICATION_REASON_URI, GTFS_ALERTTYPES_URI, INCIDENT_ADD_URI, ALL_NET_LINE_URI, LINE_STATION_URI) {
    //set default data on the page
    $scope.pageIsNew = true;
    initPageData();
    initTranspositionGrid();
    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            $scope.incident = {};
            $scope.incident.transposition = false;
            $scope.pageIsNew = true;
        } else {
            $scope.incident = angular.copy(baseDataService.getRow());
            baseDataService.setIsPageNew(true);
            baseDataService.setRow({});
            $scope.pageIsNew = false;
        }

        $scope.tripSet = [
            {
                "id" :"1",
                "name" :"trip1",
                "description" : 'Trip 1'
            },
            {
                "id" :"2",
                "name" :"trip2",
                "description" : 'Trip 2'
            }
        ]
        $scope.incident.trips = baseDataService.populateMultiSelectList($scope.incident.trips,$scope.tripSet);

        baseDataService.getBaseData(NOTIFICATION_REASON_URI).then(function(response){
            $scope.reasonSet = response.data;
            $scope.incident.reason = baseDataService.populateSelectList($scope.incident.reason,$scope.reasonSet);
            $scope.changeDueTo();
        });
        baseDataService.getBaseData(GTFS_ALERTTYPES_URI).then(function(response){
            $scope.transpositionImpactSet = response.data;
            $scope.incident.transpositionImpact = baseDataService.populateSelectList($scope.incident.transpositionImpact,$scope.transpositionImpactSet);
        });
        baseDataService.getBaseData(ALL_NET_LINE_URI).then(function(response){
            $scope.lineSet = response.data;
            $scope.incident.linesAfected = baseDataService.populateMultiSelectList($scope.incident.linesAfected,$scope.lineSet);
            $scope.changeLine('reasonDueTo');
        });
        if ($scope.pageIsNew) {
            baseDataService.getBaseData(ALL_SCRIPT_URI).then(function(response){
                    $scope.incident.scriptList = angular.copy(response.data);
            });
        }

    }
    function initTranspositionGrid() {
        $scope.gridOptions = {
            columnDefs: [
                {field:'transpositionId', visible:false, enableCellEdit:false},
                {field:'tripName',enableCellEdit:false},
                {field:'impact.name', displayName:'Impcat',enableCellEdit:false},
                {field:'stations',
                    cellTemplate:'<i tooltip="{{row.entity.stations}}" tooltip-placement="left" >' +
                        '<div >' +
                        ' {{COL_FIELD}}' +
                        '</div>' +
                        '</i>',

                    enableCellEdit:false}
            ]
        }
        $scope.gridOptions.enableRowSelection = true;
        $scope.gridOptions.multiSelect = false;
        $scope.gridOptions.noUnselect= true;

        //
        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                baseDataService.setRow(row.entity);
            });
            gridApi.cellNav.on.navigate($scope, function(newRowCol, oldRowCol){
            });
        };

        if ($scope.pageIsNew == false) {
            $scope.gridOptions.data = $scope.incident.transpositions;
        }

    }
    $scope.changeDueTo = function (){
        var uri = ALL_CATEGORY_OFTYPE_URI + $scope.incident.reason.name;
        baseDataService.getBaseData(uri).then(function(response){
            $scope.dueToSet = response.data;
            $scope.incident.dueTo = baseDataService.populateSelectList($scope.incident.dueTo, $scope.dueToSet);
            $scope.scriptChanged()
        });

    };


    $scope.addTransposition= function() {
        //extract selected stations:
        var extractedStations = baseDataService.arrayToCommaSeparatedFormat($scope.incident.affectedStaions);
        for (var i=0; i<$scope.incident.trips.length; i++) {
            var trip = $scope.incident.trips[i];
            tranaposition = {
                "transpositionId" : -1,
                "tripName" : trip.name,
                "impact" : $scope.incident.transpositionImpact,
                "stations" : extractedStations
            }
            $scope.gridOptions.data.push(tranaposition);

        }
    };
    $scope.removeTransposition= function () {
        var selectedRow = baseDataService.getRow();
        rowIndex = getArrIndexOf($scope.gridOptions.data, selectedRow);
        if (rowIndex>-1) {
            $scope.gridOptions.data.splice(rowIndex,1);
            baseDataService.setRowSelected(false);
        }


    };
    function getArrIndexOf(arr,item) {
        if (arr == undefined || item== undefined)
            return -1;
        for (var j = 0; j < arr.length; j++) {
            if (arr[j].tripName == item.tripName && arr[j].impactName == item.impactName && arr[j].stations == item.stations) {
                return j;
            }
        }
        return -1;
    };

    //create new incident
    $scope.createIncident = function () {

        //apply the script changes on the scriptList
        incidentService.synchScriptModel($scope.incident.scriptList,$scope);
        //incidentService.stripeScriptModel($scope.incident.scriptList);

        //add transpositions to the incident form.
        $scope.incident.transpositions = $scope.gridOptions.data;
        if ($scope.pageIsNew) {
            $scope.incident.incidentId = -1;
        }
        //set userid on the form
        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        $scope.incident.userId = userId;
        var rowObject = $scope.incident;
        baseDataService.addRow(rowObject, INCIDENT_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                incidentService.setIncident({});
                incidentService.setIsPageNew(true);
                $state.go('dashboard.listIncident');
            } else {
                alert('Not able to save incident. ' + addResponse.message);
            }
        });
        return;
    };
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    };
    $scope.changeLine = function (){
        var rowObject = $scope.incident.linesAfected;
        baseDataService.addRow(rowObject, LINE_STATION_URI).then(function(response) {
            $scope.stationSet = response.data;
            $scope.incident.affectedStaions = baseDataService.populateMultiSelectList($scope.incident.affectedStaions,$scope.stationSet);
        });
    };

    $scope.scriptChanged = function (tagName){
        if (tagName.indexOf("notification_script_location") >-1 || tagName.indexOf("reasonDueTo") >-1 ) {
            var script_location_value = 'at ';
            var reason_value = $scope.incident.dueTo.name;
            if (document.getElementById("notification_script_location_0").checked) {
                var element1 = document.getElementById("notification_script_location_0_0");
                script_location_value ='at ' + element1.options[element1.selectedIndex].text;
            }
            if (document.getElementById("notification_script_location_1").checked) {
                var element1 = document.getElementById("notification_script_location_1_0");
                var element2 = document.getElementById("notification_script_location_1_1");

                script_location_value = 'between '+ element1.options[element1.selectedIndex].text + ' and ' + element2.options[element2.selectedIndex].text;
            }
            $scope.incident.name = script_location_value + ': ' + reason_value;
        }

    };

    $scope.dateTimeError = function()
    {
        startDate = $scope.incident.dateTimeStart;
        endDate = $scope.incident.dateTimeEnd;

        if (startDate=='' || endDate=='' || startDate==undefined || endDate==undefined)
            return false;

        var currentDate = new Date();
        var dateOne= baseDataService.strToDate(startDate);
        var dateTwo= baseDataService.strToDate(endDate);
        if (dateOne<currentDate || dateTwo<currentDate) {
            return true;
        }

        if (dateOne>dateTwo)
            return true;
        else
            return false;
    };
})
