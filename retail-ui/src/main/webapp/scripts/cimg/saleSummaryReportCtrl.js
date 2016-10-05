/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('saleSummaryReportCtrl', function($scope, $state, baseDataService, SUCCESS, FAILURE, SALE_SUMMARY_REPORT_URI) {

    initCharts();

    function initCharts() {
        $scope.totalSaleSummaryRpt = {
            labels:['Total Items', 'Total Cost', 'Total Profit', 'Total Taxable', 'Total Tax', 'Total Sale'],
            data :[]
        }

        $scope.operatorSaleQtyRpt = {
            labels:[],
            data :[]
        }

        $scope.operatorSaleAndEGPRpt = {
            labels:[],
            series: ['Sale', 'eGP', 'eGP%'],
            data :[]
        }
    }

    $scope.staffSaleSummary = {
        enableFiltering: true,
        columnDefs: [
            {field:'toopOperator.usrFirstName', displayName:'First Name', enableCellEdit:false, width:'10%'},
            {field:'toopOperator.usrSurName', displayName:'SurName',enableCellEdit:false, width:'15%'},
            {field:'toopSaleQty', displayName:'Total Items', width:'10%',cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'toopItemsValue', displayName:'Total Cost',enableCellEdit:false, width:'12%',cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'toopProfitValue', displayName:'Total Profit',enableCellEdit:false, width:'12%',cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'toopTaxedValue', displayName:'Total Taxable',enableCellEdit:false, width:'13%',cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'toopTaxPaid', displayName:'Total Tax',enableCellEdit:false, width:'13%',cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'toopSaleValue', displayName:'Total Sale',enableCellEdit:false, width:'15%',cellFilter: 'currency', footerCellFilter: 'currency'}
        ]
    }
    $scope.staffSaleSummary.enableRowSelection = true;
    $scope.staffSaleSummary.multiSelect = false;
    $scope.staffSaleSummary.noUnselect= true;


    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }



    $scope.doReport = function() {
        var reportForm = $scope.saleSummaryReportForm;
        baseDataService.addRow(reportForm, SALE_SUMMARY_REPORT_URI).then(function(response){
            var saleSummaryData = angular.copy(response.data);
            $scope.staffSaleSummary.data = saleSummaryData.totalOperatorSaleFigures;
            makeupCharts(saleSummaryData);
        });
    }

    function makeupCharts(saleSummaryReport) {
        //buildup the total sale summary report
        if (saleSummaryReport != undefined && saleSummaryReport.totalSaleFigures != undefined ) {
            $scope.totalSaleSummaryRpt.data.push(saleSummaryReport.totalSaleFigures.toopSaleQty);
            $scope.totalSaleSummaryRpt.data.push(saleSummaryReport.totalSaleFigures.toopItemsValue);
            $scope.totalSaleSummaryRpt.data.push(saleSummaryReport.totalSaleFigures.toopProfitValue);
            $scope.totalSaleSummaryRpt.data.push(saleSummaryReport.totalSaleFigures.toopTaxedValue);
            $scope.totalSaleSummaryRpt.data.push(saleSummaryReport.totalSaleFigures.toopTaxPaid);
            $scope.totalSaleSummaryRpt.data.push(saleSummaryReport.totalSaleFigures.toopSaleValue);
        }

        //build up charts per operator
        if (saleSummaryReport.totalOperatorSaleFigures != undefined) {
            var saleData = [];
            var eGPData = [];
            var eGPPercentData = [];


            var arr = saleSummaryReport.totalOperatorSaleFigures;
            for (var i = 0; i < arr.length; i++) {
                $scope.operatorSaleQtyRpt.labels.push(arr[i].toopOperator.usrFirstName + ' ' + arr[i].toopOperator.usrSurName);
                $scope.operatorSaleQtyRpt.data.push(arr[i].toopSaleQty);


                $scope.operatorSaleAndEGPRpt.labels.push(arr[i].toopOperator.usrFirstName + ' ' + arr[i].toopOperator.usrSurName);

                saleData.push(arr[i].toopSaleValue);
                eGPData.push(arr[i].toopProfitValue);
                eGPPercentData.push( (arr[i].toopProfitValue*1/arr[i].toopSaleValue)*100 );

                $scope.operatorSaleAndEGPRpt.data.push(saleData);
                $scope.operatorSaleAndEGPRpt.data.push(eGPData);
                $scope.operatorSaleAndEGPRpt.data.push(eGPPercentData);
            }
        }


    }

});
