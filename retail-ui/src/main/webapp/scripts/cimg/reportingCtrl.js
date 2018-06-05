/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('reportingCtrl', function($scope, baseDataService, ngDialog, SUCCESS, FAILURE, REPORTING_GET_REPORT_LIST_URI, SUPPLIER_ALL_URI, CUSTOMER_ALL_URI, REPORTING_RUN_REPORT_URI) {

    $scope.reportFactor = {};
    $scope.selectedProductGroups = [];
    $scope.productGroupTreeOptions = {
        multipleSelect: true
    };

    $scope.$on('selection-changed', function(e, node) {
        $scope.selectedNode = node;
        //for some reports we need to set the default dates:
        populateDates();
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
            template:'views/pages/productGroup.html',
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
    function populateReportParams() {
        var paramList = angular.copy($scope.selectedNode.reportParamList);
        var reportNode = angular.copy($scope.selectedNode);
        reportNode.children = [];
        for (var i=0; i < paramList.length ; i++ ) {

            switch (paramList[i].repParamName) {
                case 'Date' :
                    //check if date entered
                    if ($scope.reportFactor.dateFrom != undefined) {
                        setParamValByKey(paramList[i].reportParamValList, 'DateFrom', $scope.reportFactor.dateFrom);
                    }
                    if ($scope.reportFactor.dateTo != undefined) {
                        setParamValByKey(paramList[i].reportParamValList, 'DateTo', $scope.reportFactor.dateTo);
                    }
                    break;
                case 'Range' :
                    //check if ranged entered
                    if ($scope.reportFactor.rangeFrom != undefined) {
                        setParamValByKey(paramList[i].reportParamValList, 'RangeFrom', $scope.reportFactor.rangeFrom);
                    }
                    if ($scope.reportFactor.rangeTo != undefined) {
                        setParamValByKey(paramList[i].reportParamValList, 'RangeTo', $scope.reportFactor.rangeTo);
                    }
                    break;
                case 'Category' :
                    break;
                case 'Supplier' :
                    if ($scope.reportFactor.supplierList != undefined && $scope.reportFactor.supplierList.length > 0 ) {
                        for (var j=0; j < $scope.reportFactor.supplierList.length ; j++ ) {
                            var paramVal = {
                                repParamVal : $scope.reportFactor.supplierList[j].id
                            }
                            paramList[i].reportParamValList.push(paramVal);
                        }
                    }
                    break;
                case 'Customer' :
                    if ($scope.reportFactor.customerList != undefined && $scope.reportFactor.customerList.length > 0 ) {
                        for (var j=0; j < $scope.reportFactor.customerList.length ; j++ ) {
                            var paramVal = {
                                repParamVal: $scope.reportFactor.customerList[j].id
                            }
                            paramList[i].reportParamValList.push(paramVal);
                        }
                    }
                    break;
                case 'Staff' :
                    break;
                case 'GroupBy' :
                    paramList[i].reportParamValList = [];
                    if ($scope.reportFactor.groupBy != undefined) {
                        paramList[i].reportParamValList.push($scope.reportFactor.groupBy)
                    }
                    break;
                case 'SortBy' :
                    paramList[i].reportParamValList = [];
                    if ($scope.reportFactor.sortByPrimary != undefined) {
                        paramList[i].reportParamValList.push($scope.reportFactor.sortByPrimary)
                    }
                    if ($scope.reportFactor.sortBySecondary != undefined) {
                        paramList[i].reportParamValList.push($scope.reportFactor.sortBySecondary)
                    }
                    break;
            }
        }
        reportNode.reportParamList = paramList;
        return reportNode;
    }

    function setParamValByKey(paramValList, key, value) {
        if (paramValList == undefined || key == undefined || value == undefined) {
            return;
        }
        for (var i=0; i < paramValList.length ; i++ ) {
            if (paramValList[i].repParamKey == key) {
                paramValList[i].repParamVal = value;
            }
        }
    }
    $scope.submitReport = function () {
        if ($scope.selectedNode === undefined || $scope.selectedNode.nodeType==='REPORT_GROUP' || $scope.selectedNode.reportParamList===undefined) {
            return;
        }
        var reportNode = populateReportParams();
        var exportUrl = REPORTING_RUN_REPORT_URI;
        trimTreeViewNodeObject(reportNode);
        baseDataService.pdfViewerPostMethod(exportUrl, reportNode);
    }
    function populateDates() {
        if ($scope.selectedNode == undefined ) {
            return;
        }
        if ($scope.selectedNode.reportKey === 'SALES_BY_MONTH') {
            $scope.reportFactor.dateTo = new Date();
            var dateFrom = new Date();
            dateFrom.setFullYear($scope.reportFactor.dateTo.getUTCFullYear() - 1, $scope.reportFactor.dateTo.getUTCMonth(), 1);
            $scope.reportFactor.dateFrom = dateFrom;
        }
    }
});
