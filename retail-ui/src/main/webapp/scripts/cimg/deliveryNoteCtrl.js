/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('deliveryNoteCtrl', function($filter, $scope,uiGridConstants, $state,ngDialog, $timeout,baseDataService, SUCCESS, FAILURE, DEL_NOTE_SAVE_URI, DLV_NOTE_STATUS_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" ui-grid-selection style ="height: 100px;"></div>',
        expandableRowScope:{
            subGridVariable: 'subGridScopeVariable'
        } ,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'purchaseItem.catalogueNo', displayName:'Catalogue No', enableCellEdit:false, width:'40%'},
            {field:'dlnlProductSize.unomDesc', displayName:'Product Size', enableCellEdit:false, width:'10%'},
            {field:'dlnlCaseSize.unomDesc', displayName:'Case Size', enableCellEdit:false, width:'10%'},
            {field:'dlnlUnitCost', displayName:'Case Cost',enableCellEdit:true, width:'10%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'dlnlQty', displayName:'Del Qty',enableCellEdit:true, width:'10%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'rcvdCaseSize.unomDesc', displayName:'Recd Case Size', enableCellEdit:false, width:'10%'},
            {field:'rcvdQty', displayName:'Rec Qty',enableCellEdit:true, width:'10%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'totalCost', displayName:'Total Cost',enableCellEdit:false, width:'10%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'dlnlDiscrepancy', displayName:'',enableCellEdit:false, width:'10%'},
            {name:'Action', sortable:false,enableFiltering:false,enableCellEdit:false, cellTemplate:'<a href=""><i tooltip="delete item" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-click="grid.appScope.removeItem(row)" ></i></a>', width: '5%'}
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
        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
            if (colDef.name == 'dlnlQty') {
                $scope.dlnlQtyBeforeEditting = rowEntity.dlnlQty;
            }
            if (colDef.name == 'rcvdQty') {
                $scope.rcvdQtyBeforeEditting = rowEntity.rcvdQty;
            }
        })
    };
    $scope.$on('uiGridEventEndCellEdit', function (event) {
        var deliveryNoteLine = event.targetScope.row.entity;
        //update the total value of the line
        updateLineTotalCost(deliveryNoteLine);
    })

    initPageData();
    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            $scope.deliveryNoteHeader = {};
            $scope.deliveryNoteHeader.id = -1;
            $scope.pageIsNew = true;
        } else {
            $scope.deliveryNoteHeader = angular.copy(baseDataService.getRow());
            $scope.gridOptions.data = $scope.deliveryNoteHeader.lines;
            baseDataService.setRow({});
            baseDataService.setIsPageNew(true);
            $scope.pageIsNew = false;
        }
        baseDataService.getBaseData(DLV_NOTE_STATUS_URI).then(function(response){
            $scope.delnStatusSet = response.data;
            $scope.deliveryNoteHeader.delnStatus = baseDataService.populateSelectList($scope.deliveryNoteHeader.delnStatus,$scope.delnStatusSet);
        });
    }

    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }

    $scope.searchPurchaseOrder = function () {
        ngDialog.openConfirm({
            template:'views/pages/purchaseOrderSearch.html',
            controller:'purchaseOrderSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {supplier: function(){return $scope.deliveryNoteHeader.supplier}}
        }).then (function (selectedItem){
                if (selectedItem != undefined) {
                    var purchaseOrderHeader = selectedItem;
                    populateDateFromPurchaseOrder(purchaseOrderHeader);
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    function populateDateFromPurchaseOrder (purchaseOrder) {

        $scope.deliveryNoteHeader.pohOrderNumber = purchaseOrder.pohOrderNumber;
        $scope.deliveryNoteHeader.pohId = purchaseOrder.id;
        for (var i = 0; i < purchaseOrder.lines.length; i++) {
            var createdLine = createDeliveryNoteLine(purchaseOrder.lines[i]);
            updateLineTotalCost(createdLine);
            $scope.gridOptions.data.push(createdLine);
        }
    }

    function createDeliveryNoteLine (purchaseOrderLine) {
        var rowId;
        if ($scope.gridOptions.data == undefined && $scope.gridOptions.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.gridOptions.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        var deliveryNoteLineObject = {
            'id':rowId,
            'delnId':$scope.deliveryNoteHeader.id,
            'polId':purchaseOrderLine.id,
            'purchaseItem' : purchaseOrderLine.purchaseItem,
            'dlnlCaseSize' : purchaseOrderLine.unitOfMeasure,
            'dlnlProductSize' : purchaseOrderLine.unomContents,
            'dlnlUnitCost' : purchaseOrderLine.polUnitCost,
            'dlnlQty' : purchaseOrderLine.polQtyOrdered,
            'rcvdCaseSize' : purchaseOrderLine.unitOfMeasure,
            'rcvdProductSize' :  $scope.unomContents,
            'rcvdQty' :  purchaseOrderLine.polQtyOrdered
        }
        return deliveryNoteLineObject;
    }

    function updateLineTotalCost(line) {
        if (line == undefined) {
            return;
        }
        line.totalCost = line.dlnlUnitCost * line.rcvdQty;
    }
    $scope.saveDeliveryNote = function () {

        /*
         var userId = UserService.getUserId();
         if (userId == undefined || userId == 0) {
         alert('you need to login first');
         $state.go('dashboard.login');
         }
         */

        //$scope.facility.lastModifiedBy = userId;
        var rowObject = $scope.deliveryNoteHeader;
        if ($scope.pageIsNew) {
            $scope.deliveryNoteHeader.lines = $scope.gridOptions.data
        }
        baseDataService.addRow(rowObject, DEL_NOTE_SAVE_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                if ($scope.pageIsNew) {
                    baseDataService.displayMessage("GRN Number", "Delivery Note saved with GRN: " + addResponse.info);
                }
                $state.go('dashboard.deliveryNoteList');
            } else {
                alert('Not able to save DeliveryNote. ' + addResponse.message);
            }
        });
        return;
    }

    $scope.removeItem = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('item is undefined');
            return;
        }
        if (!confirm('Are you sure you want to delete this item?')) {
            return;
        }
        var rowIndex = baseDataService.getArrIndexOf($scope.deliveryNoteHeader.lines, row.entity);
        if (rowIndex>-1) {
            $scope.deliveryNoteHeader.lines.splice(rowIndex,1);
        }
    }

    $scope.searchSupplier = function () {
        ngDialog.openConfirm({
            template:'views/pages/supplierSearch.html',
            controller:'supplierSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                //alert('returned value = ' + value);
                $scope.deliveryNoteHeader.supplier = value;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

});
