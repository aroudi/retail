/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('reportingCtrl', function($scope, baseDataService, ngDialog, SUCCESS, FAILURE, REPORTING_GET_REPORT_LIST_URI) {

    $scope.productGroupTreeOptions = {
        multipleSelect: true
    };

    $scope.$on('selection-changed', function(e, node) {
        $scope.selectedNode = node;
        enableFilters();
    });

    function initReportFilters() {
        $scope.dateFilterEnabled = false;
        $scope.rangeFilterEnabled = false;
        $scope.categoryFilterEnabled = false;
        $scope.supplierFilterEnabled = false;
        $scope.customerFilterEnabled = false;
        $scope.staffFilterEnabled = false;
        $scope.groupByFilterEnabled = false;
        $scope.sortByFilterEnabled = false;
    }

    loadReportingList();
    function loadReportingList() {
        baseDataService.getBaseData(REPORTING_GET_REPORT_LIST_URI).then(function(response){
            $scope.reportTree = response.data;
        });
        initReportFilters();
    }

    function trimTreeViewNodeObject(treeViewNode) {
        delete treeViewNode.parentId;
        delete treeViewNode.nodeId;
        delete treeViewNode.selected;
        delete treeViewNode.expanded;
        delete treeViewNode.children;
    }

    function enableFilters() {
        initReportFilters();

        if ($scope.selectedNode === undefined || $scope.selectedNode.nodeType==='REPORT_GROUP' || $scope.selectedNode.reportParamList===undefined) {
            return;
        }

        if ($scope.selectedNode.reportParamList.length < 1) {
            return;
        }

        for (var i=0; i < $scope.selectedNode.reportParamList.length ; i++ ) {

            switch ($scope.selectedNode.reportParamList[i].repParamName) {
                case 'Date' :
                    $scope.dateFilterEnabled = true;
                    break;
                case 'Range' :
                    $scope.rangeFilterEnabled = true;
                    break;
                case 'Category' :
                    $scope.categoryFilterEnabled = true;
                    break;
                case 'Supplier' :
                    $scope.supplierFilterEnabled = true;
                    break;
                case 'Customer' :
                    $scope.customerFilterEnabled = true;
                    break;
                case 'Staff' :
                    $scope.staffFilterEnabled = true;
                    break;
                case 'GroupBy' :
                    $scope.groupByFilterEnabled = true;
                    break;
                case 'SortBy' :
                    $scope.sortByFilterEnabled = true;
                    break;
            }
        }
    }

});
