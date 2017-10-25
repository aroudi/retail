/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productAuditTrailCtrl', function($scope, $state, $timeout, ngDialog, uiGridConstants, baseDataService, searchUrl, TXN_GET_URI, INVOICE_GET_URI, DEL_NOTE_GET_URI, TAXLEGVARIANCE_ALL_URI) {
    $scope.title = "Audit Trail";
    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:false,
        enableRowSelection:false,
        selectionRowHeaderWidth:35,
        enableColumnResizing: true,
        showGridFooter:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'stckEvntDate', displayName:'Date',enableCellEdit:false, width:'15%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'transactionType', displayName:'Explanation', enableCellEdit:false, width:'15%', cellFilter:'configCategoryFilter'},
            {field:'stockCategory', displayName:'Stock', enableCellEdit:false, width:'15%', cellFilter:'configCategoryFilter'},
            {field:'txnNumber', displayName:'Source',enableCellEdit:false, width:'10%'},
            {field:'stckQty', displayName:'Move',enableCellEdit:false, width:'15%'},
            {field:'sellPrice', displayName:'Price',enableCellEdit:false, width:'15%'},
            {name:'Action', enableFiltering:false, cellTemplate:'<a href=""><i tooltip="View" tooltip-placement="bottom" class="fa fa-eye fa-2x" ng-click="grid.appScope.viewSourceTxn(row)"></i></a>', width:'12%' }
        ]
    };
    $scope.gridOptions.multiSelect = false;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
    };
    getRows();
    function getRows() {
        baseDataService.getBaseData(searchUrl).then(function(response){
           $scope.gridOptions.data = response.data;
        });
    }
    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    };

    $scope.viewSourceTxn = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        switch (row.entity.transactionType.categoryCode) {
            case 'TXN_TYPE_SALE' :
                viewSaleOrder(row);
                break;

            case 'TXN_TYPE_REFUND' :
                viewInvoice(row);
                break;

            case 'TXN_TYPE_INVOICE' :
                viewInvoice(row);
                break;

            case 'TXN_TYPE_GOODS_IN' :
                viewDeliveryNoteDetail(row);
                break;

        }
    };

    function viewSaleOrder(row) {
        var txnSaleGetURI = TXN_GET_URI + row.entity.txnHeader;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            ngDialog.openConfirm({
                template:'views/pages/txnSale.html',
                controller:'txnSaleCtrl',
                className: 'ngdialog-pdfView',
                closeByDocument:false,
                resolve: {viewMode: function(){return true}}
            }).then (function (){
                }, function(reason) {
                    console.log('Modal promise rejected. Reason:', reason);
                }
            );
        });
    };

    function viewInvoice(row) {
        var txnSaleGetURI = INVOICE_GET_URI + row.entity.txnHeader;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
        //redirect to the supplier page.
            ngDialog.openConfirm({
                template:'views/pages/txnSale.html',
                controller:'txnSaleCtrl',
                className: 'ngdialog-pdfView',
                closeByDocument:false,
                resolve: {viewMode: function(){return true}}
            }).then (function (){
                }, function(reason) {
                    console.log('Modal promise rejected. Reason:', reason);
                }
            );
        });
    };

    function viewDeliveryNoteDetail(row) {
        var delnGetURI = DEL_NOTE_GET_URI +  row.entity.txnHeader;
        baseDataService.getBaseData(delnGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            ngDialog.openConfirm({
                template:'views/pages/deliveryNote.html',
                controller:'deliveryNoteCtrl',
                className: 'ngdialog-pdfView',
                closeByDocument:false,
                resolve: {viewMode: function(){return true},
                    taxCodeSet: function(baseDataService, TAXLEGVARIANCE_ALL_URI){
                        return baseDataService.getBaseData(TAXLEGVARIANCE_ALL_URI);
                    }
                }
            }).then (function (){
                }, function(reason) {
                    console.log('Modal promise rejected. Reason:', reason);
                }
            );
        });
    };


});
