/**
 * Created by arash on 14/08/2015.
 */
function tlsNBPublishController($scope, $state, UserService, baseDataService, SUCCESS, TLSOTR_GETLINEGROUP_URI, TLSOTR_GETTRIP_URI ) {
    //set default data on the page
    initPageData();
    function initPageData() {
        $scope.treeOptions = {
            nodeChildren :"children",
            dirSelectable : true,
            multiSelection : true,
            allowDeselect:true,
            injectClasses: {
                ul:"a1",
                li:"a2",
                liSelected:"a7",
                iExpanded:"a3",
                iCollapsed:"a4",
                iLeaf:"a5",
                label:"a6",
                labelSelected:"a8"
            }
        }
        $scope.tlsNbForm = angular.copy(baseDataService.getRow());
        baseDataService.setRow({});
        baseDataService.getBaseData(TLSOTR_GETLINEGROUP_URI).then(function(response){
            $scope.tlsNbForm.lineGroups = response.data;
        });
        $scope.tlsNbForm.selectedNodes = [];

    }
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    };

    //retreive list of trips
    $scope.getTripList = function () {

        var userName = UserService.getUserName();
        if (userName == undefined ) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        $scope.tlsNbForm.userName = userName;
        var rowObject = $scope.tlsNbForm;
        baseDataService.addRow(rowObject, TLSOTR_GETTRIP_URI).then(function(response) {
            $scope.tlsNbForm.trips = response.data;
        });
    }

}
