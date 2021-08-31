/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('pricingGradeListCtrl', function($scope, $state, baseDataService, SUCCESS, FAILURE, CUSTOMERGRADE_ALL_URI, PRICING_GRADE_UPDATE_BATCH_URI, PRICING_RULE_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'gradeCode', displayName:'Grade', enableCellEdit:false, width:'15%'},
            {field:'description', displayName:'Description',editType:'dropdown',enableCellEdit:true, width:'30%',
                editableCellTemplate:'<select class="form-control" data-ng-model="row.entity.pricingRule"  ng-options="rule.categoryCode for rule in grid.appScope.pricingRuleSet" > </select>'            },
            {field:'rate', displayName:'Rate', type: 'number',width:'10%'}
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
        });
    };
    //set default data on the page
    initPageData();
    function initPageData() {
        baseDataService.getBaseData(CUSTOMERGRADE_ALL_URI).then(function(response){
            $scope.gridOptions.data = response.data;
        });
        baseDataService.getBaseData(PRICING_RULE_URI).then(function(response){
            $scope.pricingRuleSet = response.data;
        });
    }

    $scope.update = function () {
        for (i=0; i<$scope.gridOptions.data.length; i++) {
            if ($scope.gridOptions.data[i].pricingRule != undefined || $scope.gridOptions.data[i].pricingRule != null) {
                $scope.gridOptions.data[i].description = $scope.gridOptions.data[i].pricingRule.categoryCode;
                delete $scope.gridOptions.data[i].pricingRule;
            }
        }
        baseDataService.addRow($scope.gridOptions.data, PRICING_GRADE_UPDATE_BATCH_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                //$state.go('dashboard.pricingGradeList');
                //baseDataService.displayMessage('info','Info','List updated successfully');
                //
                $state.go('dashboard.firstPage');
            } else {
                baseDataService.displayMessage('info','Warning', 'Not able to update grade. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }
});
