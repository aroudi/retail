/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('reportingCtrl', function($scope, baseDataService, ngDialog, SUCCESS, FAILURE, REPORTING_GET_REPORT_LIST_URI, SUPPLIER_ALL_URI, CUSTOMER_ALL_URI) {

    $scope.selectedProductGroups = [];
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
        $scope.selectedProductGroups = [];
    }

    loadReportingList();
    function loadReportingList() {
        baseDataService.getBaseData(REPORTING_GET_REPORT_LIST_URI).then(function(response){
            $scope.reportTree = response.data;

        });
        baseDataService.getBaseData(SUPPLIER_ALL_URI).then(function(response){
            $scope.supplierSet = response.data;
        });
        baseDataService.getBaseData(CUSTOMER_ALL_URI).then(function(response){
            $scope.customerSet = response.data;
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
                    $scope.groupBySet = $scope.selectedNode.reportParamList[i].reportParamValList;
                    break;
                case 'SortBy' :
                    $scope.sortByFilterEnabled = true;
                    $scope.sortBySet = $scope.selectedNode.reportParamList[i].reportParamValList;
                    break;
            }
        }
    }
    $scope.selectProductGroup = function () {
        ngDialog.openConfirm({
            template:'views/pages/ProductGroupTree.html',
            controller:'productGroupsCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (result){
                $scope.selectedProductGroups = result;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }
});
