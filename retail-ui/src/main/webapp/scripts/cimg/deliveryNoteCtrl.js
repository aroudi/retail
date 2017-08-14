/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('deliveryNoteCtrl', function($filter, $scope,uiGridConstants, $state,ngDialog,viewMode, $timeout,baseDataService,multiPageService, SUCCESS, FAILURE, DEL_NOTE_SAVE_URI, DLV_NOTE_STATUS_URI, POH_GET_ALL_CONFIRMED_PER_SUPPLIER_URI, TAXRULE_ALL_URI, SUPPLIER_ALL_URI, taxCodeSet, GET_PURCHASE_ITEM_PER_SUPPLIER_CATALOG_URI, TAXLEGVARIANCE_GST_URI, GET_PURCHASE_ITEMS_PER_SUPPLIER_URI, GET_PURCHASE_ITEM_PER_ID_URI) {
    $scope.taxLegVarianceSet = taxCodeSet.data;
    $scope.isViewMode = false;
    if (viewMode!=undefined) {
        $scope.isViewMode = viewMode;
    }

    $scope.gridOptions = {
        enableFiltering: false,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableCellEditOnFocus:true,
        enableSorting:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'purchaseItem.catalogueNo', displayName:'Catalogue No', enableCellEdit:false,enableFiltering:false, width:'22%'},
            //{field:'dlnlProductSize.unomDesc', displayName:'Product Size', enableCellEdit:false, width:'8%'},
            {field:'dlnlCaseSize.unomDesc', displayName:'Size', enableCellEdit:false, enableFiltering:false,width:'5%'},
            {field:'dlnlUnitCost', displayName:'Cost',enableCellEdit:true, width:'5%', enableFiltering:false,cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'taxLegVariance.txlvDesc',editType:'dropdown', displayName:'Tax',enableCellEdit:true,width:'8%',enableFiltering:false,
                editableCellTemplate:'<select class="form-control" data-ng-model="row.entity.taxLegVariance" ng-change="grid.appScope.updateLineTotalCost(row.entity)" ng-options="tax.txlvDesc for tax in grid.appScope.taxLegVarianceSet" > </select>',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
                //cellTemplate:'<select class="form-control" data-ng-model="row.entity.taxLegVariance"  ng-options="tax.txlvDesc for tax in grid.appScope.taxLegVarianceSet" > </select>'
            },
            //polQtyOrdered
            {field:'polQty', displayName:'Purchased',enableCellEdit:false, width:'6%',type: 'number',enableFiltering:false, aggregationType: uiGridConstants.aggregationTypes.sum},
            //{field:'rcvdCaseSize.unomDesc', displayName:'Recd Case Size', enableCellEdit:false, width:'8%'},
            {field:'rcvdQty', displayName:'Rec Qty',enableCellEdit:true, width:'5%',type: 'number',enableFiltering:false, aggregationType: uiGridConstants.aggregationTypes.sum,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'backOrder()', displayName:'Back Order',enableCellEdit:false, width:'8%',type: 'number',enableFiltering:false, aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'delnValueTax', displayName:'Tax value',enableCellEdit:false, width:'7%',enableFiltering:false, cellFilter: 'currency'},
            {field:'totalCost', displayName:'Total Cost',enableCellEdit:false, width:'8%', cellFilter: 'currency', footerCellFilter: 'currency', enableFiltering:false,aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'dlnlDiscrepancy', displayName:'Discrepancy',enableCellEdit:false, type:'boolean', width:'6%',cellFilter:'booleanFilter', cellTemplate:'<input type="checkbox" ng-model="row.entity.dlnlDiscrepancy">',enableFiltering:false,
                cellClass:
                    function(grid, row, col, rowRenderIndex, colRenderIndex) {
                        if (grid.getCellValue(row, col) === true) {
                            return 'red';
                        } else {
                            return 'green'
                        }
                    }
            },
            {field:'dlnlComment', displayName:'Location',enableCellEdit:true, width:'15%', enableFiltering:false,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {name:'Action', sortable:false,enableFiltering:false,enableCellEdit:false, cellTemplate:'<a href=""><i tooltip="delete item" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-click="grid.appScope.removeItem(row)" ng-disabled="disablePage"></i></a>', width: '5%'}
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
        //dlnlDiscrepancy
        if (deliveryNoteLine.dlnlQty == deliveryNoteLine.rcvdQty) {
            deliveryNoteLine.dlnlDiscrepancy = false;
        } else {
            deliveryNoteLine.dlnlDiscrepancy = true;
        }

        //update the total value of the line
        $scope.updateLineTotalCost(deliveryNoteLine);
    })

    initPageData();
    function initPageData() {
        $scope.disablePage = false;
        if ( baseDataService.getIsPageNew()) {
            $scope.deliveryNoteHeader = {};
            $scope.deliveryNoteHeader.deliveryDate = new Date();
            $scope.deliveryNoteHeader.id = -1;
            $scope.deliveryNoteHeader.freightAmount = 0.00;
            $scope.pageIsNew = true;
            $scope.deliveryNoteHeader.delnSurcharge = 0.00;
            $scope.deliveryNoteHeader.costsIncludeTax = false;
        } else {
            $scope.deliveryNoteHeader = angular.copy(baseDataService.getRow());
            $scope.gridOptions.data = $scope.deliveryNoteHeader.lines;
            angular.forEach($scope.gridOptions.data,function(row){
                row.backOrder = function() {
                    return this.polQty - this.rcvdQty;
                }
            });
            $scope.deliveryNoteHeader.deliveryDate = new Date($scope.deliveryNoteHeader.deliveryDate);
            baseDataService.setRow({});
            baseDataService.setIsPageNew(true);
            $scope.pageIsNew = false;
            if ($scope.deliveryNoteHeader.delnStatus.categoryCode == 'DLV_NOTE_STATUS_COMPLETE') {
                $scope.disablePage = true;
            } else {
                $scope.disablePage = false;
            }
            if ($scope.deliveryNoteHeader.id === -1){
                $scope.pageIsNew = true;
            } else {
                $scope.pageIsNew = false;
            }
        }
        baseDataService.getBaseData(DLV_NOTE_STATUS_URI).then(function(response){
            $scope.delnStatusSet = response.data;
            $scope.deliveryNoteHeader.delnStatus = baseDataService.populateSelectList($scope.deliveryNoteHeader.delnStatus,$scope.delnStatusSet);
        });
        baseDataService.getBaseData(TAXRULE_ALL_URI).then(function(response){
            $scope.taxRuleSet = response.data;
            $scope.deliveryNoteHeader.freightTxrl = baseDataService.populateSelectList($scope.deliveryNoteHeader.freightTxrl,$scope.taxRuleSet);
            //$scope.calculateTotal();
        });
        baseDataService.getBaseData(SUPPLIER_ALL_URI).then(function(response){
            $scope.supplierSet = response.data;
            /*
            if ($scope.supplierSet.length > 0) {
                var supplier = {
                    "id" : -1,
                    "supplierName" : "Select"
                }
                $scope.supplierSet.unshift(supplier);
            }
            */
            //$scope.deliveryNoteHeader.supplier = baseDataService.populateSelectList($scope.deliveryNoteHeader.supplier,$scope.supplierSet);
            //$scope.changeSupplier();
        });
        baseDataService.getBaseData(TAXLEGVARIANCE_GST_URI).then(function(response){
            $scope.gstTaxLegVariance = response.data;
        });
   }

    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }

    $scope.searchPurchaseOrder = function () {
        if ($scope.deliveryNoteHeader.supplier === undefined || $scope.deliveryNoteHeader.supplier.id === -1) {
            baseDataService.displayMessage('info','Warning!','Please select supplier');
            return;
        }
        //check if this supplier has outstanding purchase order.
        ngDialog.openConfirm({
            template:'views/pages/purchaseOrderSearch.html',
            controller:'purchaseOrderSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {searchUrl: function(){return POH_GET_ALL_CONFIRMED_PER_SUPPLIER_URI + $scope.deliveryNoteHeader.supplier.id}}
        }).then (function (selectedItem){
                if (selectedItem != undefined) {
                    var purchaseOrderHeader = selectedItem;
                    populateDateFromPurchaseOrder(purchaseOrderHeader);
                } else {
                    baseDataService.displayMessage('info','Warning!','No submitted purchase order found for this supplier');
                    $scope.gridOptions.data=[];
                    return;
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
            $scope.updateLineTotalCost(createdLine);
            createdLine.backOrder = function() {
                return this.polQty - this.rcvdQty;
            }
            $scope.gridOptions.data.push(createdLine);
            calculateSubTotal();
        }

    }

    function createDeliveryNoteLine (purchaseOrderLine) {
        var rowId;
        var taxLegVar;
        if ($scope.gridOptions.data == undefined && $scope.gridOptions.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.gridOptions.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        if (purchaseOrderLine.taxLegVariance == undefined || purchaseOrderLine.taxLegVariance == null) {
            taxLegVar = $scope.gstTaxLegVariance;
        } else {
            taxLegVar = purchaseOrderLine.taxLegVariance;
        }
        var deliveryNoteLineObject = {
            'id':rowId,
            'delnId':$scope.deliveryNoteHeader.id,
            'polId':purchaseOrderLine.id,
            'purchaseItem' : purchaseOrderLine.purchaseItem,
            'dlnlCaseSize' : purchaseOrderLine.unitOfMeasure,
            'dlnlProductSize' : purchaseOrderLine.unomContents,
            'dlnlUnitCost' : purchaseOrderLine.polUnitCost,
            'dlnlQty' : purchaseOrderLine.polQtyOrdered - purchaseOrderLine.polQtyReceived,
            'rcvdCaseSize' : purchaseOrderLine.unitOfMeasure,
            'rcvdProductSize' :  $scope.unomContents,
            'rcvdQty' :  purchaseOrderLine.polQtyOrdered - purchaseOrderLine.polQtyReceived,
            'dlnlDiscrepancy' : false,
            'polQty' : purchaseOrderLine.polQtyOrdered,
            'taxLegVariance' : taxLegVar,
            'delnValueTax' : purchaseOrderLine.polValueTax,
            'delnValueGross' : purchaseOrderLine.polValueGross
        }
        return deliveryNoteLineObject;
    }

    function createDeliveryNoteLineFromProduct (productPurchaseItem) {
        var rowId;
        var taxLegVar;
        if (productPurchaseItem.taxLegVariance == undefined || productPurchaseItem.taxLegVariance == null) {
            taxLegVar = $scope.gstTaxLegVariance;
        } else {
            taxLegVar = productPurchaseItem.taxLegVariance;
        }
        if ($scope.gridOptions.data == undefined && $scope.gridOptions.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.gridOptions.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        var deliveryNoteLineObject = {
            'id':rowId,
            'delnId':$scope.deliveryNoteHeader.id,
            'purchaseItem' : productPurchaseItem,
            'dlnlCaseSize' : productPurchaseItem.unitOfMeasure,
            'dlnlProductSize' : productPurchaseItem.unitOfMeasureContent,
            'dlnlUnitCost' : productPurchaseItem.costBeforeTax,
            'dlnlQty' : 1,
            'rcvdCaseSize' : productPurchaseItem.unitOfMeasure,
            'rcvdProductSize' : productPurchaseItem.unitOfMeasureContent,
            'rcvdQty' :  1,
            'dlnlDiscrepancy' : false,
            'polQty' : 0,
            'taxLegVariance' : taxLegVar,
            'delnValueTax' : 0,
            'delnValueGross' : 0
        }
        $scope.updateLineTotalCost(deliveryNoteLineObject);
        return deliveryNoteLineObject;
    }

    $scope.updateLineTotalCost = function(line) {
        if (line == undefined) {
            return;
        }

        var productsValue = line.dlnlUnitCost * line.rcvdQty;
        console.log('productsValue = ' + productsValue);
        if (line.taxLegVariance != undefined && line.taxLegVariance.txlvAmount != undefined) {
            line.delnValueTax = line.dlnlUnitCost * line.taxLegVariance.txlvRate * line.rcvdQty;
        } else {
            line.delnValueTax = 0.00;
        }
        if ($scope.deliveryNoteHeader.costsIncludeTax) {
            line.totalCost = productsValue;
            line.delnValueGross = productsValue*1 - line.delnValueTax*1
        }else {
            line.delnValueGross = productsValue;
            line.totalCost = line.delnValueGross*1 + line.delnValueTax*1;
        }



        //line.totalCost = line.dlnlUnitCost * line.rcvdQty;
        calculateSubTotal();
    }
    $scope.saveDeliveryNote = function () {


        if ($scope.disablePage) {
            baseDataService.displayMessage("info","Warning", "You can not submit an already confirmed delivery note!!!! ");
            return;
        }
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
                    baseDataService.displayMessage("info","GRN Number", "Delivery Note saved with GRN: " + addResponse.info);
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
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete this item?').then(function(result){
            if (result) {
                row.entity.deleted = true;
                $scope.gridApi.core.setRowInvisible(row);
            } else {
                return;
            }
        });
    }

    $scope.changeSupplier = function() {
        $scope.searchPurchaseOrder();
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
                $scope.searchPurchaseOrder();
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    function calculateSubTotal() {
        var lineList =  $scope.gridOptions.data;
        var valueNett = 0.00;
        var valueGross = 0.00;
        var valueTax = 0.00;
        for (var i = 0; i < lineList.length; i++) {
            if (!lineList[i].deleted) {
                valueNett = valueNett*1 + lineList[i].totalCost*1 ;
                valueGross = valueGross*1 + lineList[i].delnValueGross*1 ;
                valueTax = valueTax*1 + lineList[i].delnValueTax*1 ;
            }
        }
        $scope.deliveryNoteHeader.delnValueNett = valueNett;
        $scope.deliveryNoteHeader.delnValueGross = valueGross;
        $scope.deliveryNoteHeader.delnTax = valueTax;
        $scope.calculateTotal();
    }

    $scope.calculateTotal = function() {
        console.log('total called');
        $scope.deliveryNoteHeader.freightTax = $scope.deliveryNoteHeader.freightAmount*$scope.deliveryNoteHeader.freightTxrl.taxLegVariance.txlvRate*1
        $scope.deliveryNoteHeader.delnTotal = $scope.deliveryNoteHeader.delnValueNett*1 + $scope.deliveryNoteHeader.freightAmount*1 + $scope.deliveryNoteHeader.freightTax*1 + $scope.deliveryNoteHeader.delnSurcharge*1;
    }
    function saveAsDraft()
    {
        if ($scope.isViewMode) {
            return;
        }
        $scope.deliveryNoteHeader.lines = $scope.gridOptions.data
        var pageId;
        if ($scope.pageData === undefined) {
            pageId = -1;
        } else {
            pageId = $scope.pageData.id;
        }
        var txnType = {
            'id' : -1,
            'categoryCode' : 'PAGE_TYPE_DELIVERY_NOTE',
            'description' :'Received Note'
        }
        $scope.pageData = multiPageService.addPage(pageId, txnType, $scope.deliveryNoteHeader.delnNoteNumber ===undefined?'':$scope.deliveryNoteHeader.delnNoteNumber,$scope.deliveryNoteHeader);
    }
    var promise;
    (function refresh() {
        saveAsDraft();
        promise = $timeout(refresh, 5000);
    })();

    $scope.$on('$destroy', function () {
        $timeout.cancel(promise);
        promise = undefined;
    });
    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }
    $scope.onTaxIncludeChange = function() {
        var poLineList =  $scope.gridOptions.data;
        for (var i = 0; i < poLineList.length; i++) {
            if (!poLineList[i].deleted ) {
                $scope.updateLineTotalCost(poLineList[i]);
            }
        }
    }

    $scope.searchProduct = function () {
        var searchStr = $scope.searchByCatalog;
        if ($scope.searchByCatalog === undefined || $scope.searchByCatalog === null || $scope.searchByCatalog === "") {
            searchStr = "@ALL@";
        }
        $scope.searchByCatalog = "";

        if ($scope.deliveryNoteHeader.supplier === undefined || $scope.deliveryNoteHeader.supplier.id === -1) {
            baseDataService.displayMessage('info','Warning!','Please select supplier');
            return;
        }
        ngDialog.openConfirm({
            template:'views/pages/productSaleItemSearch.html',
            controller:'productPurchaseItemSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {searchUrl: function(){return GET_PURCHASE_ITEMS_PER_SUPPLIER_URI + $scope.deliveryNoteHeader.supplier.id + '/' + searchStr}}
        }).then (function (selectedItems){
                if (selectedItems != undefined) {
                    for (var i = 0; i < selectedItems.length; i++) {
                        var selectedProduct = selectedItems[i];
                        baseDataService.getBaseData(GET_PURCHASE_ITEM_PER_ID_URI + selectedProduct.id).then(function(response){
                            var line = createDeliveryNoteLineFromProduct(response.data);
                            $scope.gridOptions.data.push(line);
                        });
                    }
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    function checkIfProductHasBeenSelected(item) {

        for (var i = 0; i<$scope.gridOptions.data.length; i++) {
            if ($scope.gridOptions.data[i].purchaseItem.prodId == item.prodId) {
                return true;
            }
        }
        return false;
    }
    $scope.searchProductByCatalogNo = function(keyEvent) {
        if (keyEvent.which != 13) {
            return
        }
        if ($scope.deliveryNoteHeader.supplier === undefined) {
            baseDataService.displayMessage('info','Warning!','Please select supplier');
            return;
        }
        $scope.searchProduct();
    }
});
