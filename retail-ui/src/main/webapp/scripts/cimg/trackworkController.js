/**
 * Created by arash on 14/08/2015.
 */
function trackworkController($scope, $state, baseDataService, UserService, incidentService , SUCCESS, TRACKWORK_SCRIPT_URI,TRACKWORK_ADD_URI, CATEGORY_SUBTYPE_URI, ALL_NET_LINE_URI) {
    //set default data on the page
    $scope.pageIsNew = true;
    initPageData();
    function initPageData() {

        if ( baseDataService.getIsPageNew()) {
            $scope.trackwork = {};
            //$scope.trackwork.frequency = "25 18 * * 2";
            $scope.trackwork.messageType='Trackwork';
            $scope.pageIsNew = true;
        } else {
            $scope.trackwork = angular.copy(baseDataService.getRow());
            baseDataService.setIsPageNew(true);
            baseDataService.setRow({});
            $scope.pageIsNew = false;
        }
        baseDataService.getBaseData(ALL_NET_LINE_URI).then(function(response){
            $scope.lineSet = response.data;
            $scope.trackwork.lines = baseDataService.populateMultiSelectList($scope.trackwork.lines,$scope.lineSet);
        });

        baseDataService.getBaseData(CATEGORY_SUBTYPE_URI).then(function(response){
            $scope.subTypeSet = response.data;
            $scope.trackwork.subType = baseDataService.populateSelectList($scope.trackwork.subType,$scope.subTypeSet);
        });
        if ($scope.pageIsNew) {
            baseDataService.getBaseData(TRACKWORK_SCRIPT_URI).then(function(response){
                    $scope.trackwork.scriptList = angular.copy(response.data);
            });
        }

    }

    //create new trackwork
    $scope.createTrackwork = function () {

        //apply the script changes on the scriptList
        incidentService.synchScriptModel($scope.trackwork.scriptList,$scope);
        //incidentService.stripeScriptModel($scope.incident.scriptList);

        //add transpositions to the incident form.
        if ($scope.pageIsNew) {
            $scope.trackwork.incidentId = -1;
        }
        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        $scope.trackwork.userId = userId;
        var rowObject = $scope.trackwork;
        baseDataService.addRow(rowObject, TRACKWORK_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listIncident');
            } else {
                alert('Not able to save incident. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }
    $scope.buildMessageTitle = function() {
        var extractedLines = baseDataService.lineToCommaSeparatedFormat($scope.trackwork.lines);
        var messageType = $scope.trackwork.messageType=='Trackwork'? 'Trackwork' : 'Special Events'
        $scope.trackwork.title = messageType + ' - ' +extractedLines;
    }
    $scope.dateTimeError = function()
    {
        startDate = $scope.trackwork.fromDate;
        endDate = $scope.trackwork.toDate;

        if (startDate=='' || endDate=='' || startDate==undefined || endDate==undefined)
            return true;

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

    $scope.durationDateTimeError = function()
    {
        startDate = $scope.trackwork.durationFrom;
        endDate = $scope.trackwork.durationTo;

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
}
