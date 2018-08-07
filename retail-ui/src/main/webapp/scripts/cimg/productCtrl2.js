/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productCtrl2', function($scope, $state, $stateParams, UserService, baseDataService, ngDialog,viewMode, SUCCESS, FAILURE, PRODUCT_ADD_URI, PRODUCT_STATUS_URI, PRODUCT_TYPE_URI, UNOM_ALL_URI, TAXRULE_ALL_URI, taxCodeSet, PRODUCT_AUDIT_TRAIL_URI, PRODUCT_CHECK_EXISTENCE_URI,PRGP_GET_ALL_TREEVIEW_URI) {
    //set default data on the page
    $scope.taxLegVarianceSet = taxCodeSet.data;
    initPageData();
    $scope.isViewMode = false;
    if (viewMode!=undefined) {
        $scope.isViewMode = viewMode;
    }
    function initPageData() {
        $scope.category1Label = 'Category1';
        $scope.category2Label = 'Category2';
        $scope.category3Label = 'Category3';
        if ($stateParams.blankPage) {
            $scope.isNewPage = true;
            $scope.productForm = {};
            $scope.productForm.prodId = -1;
            $scope.productForm.prouId = -1;
        } else {
            $scope.isNewPage = false;
            $scope.productForm = angular.copy(baseDataService.getRow());
            if ($scope.productForm.productGroups != undefined && $scope.productForm.productGroups.length > 0) {
                $scope.department = { name: " ", id : $scope.productForm.productGroups[0].deptId };
            }
            //baseDataService.setRow({});
            //baseDataService.setIsPageNew(true);
        }
        initSupplierPriceGrid();
        //baseDataService.setIsPageNew(true);
        baseDataService.getBaseData(PRODUCT_STATUS_URI).then(function(response){
            $scope.productStatusSet = response.data;
            $scope.productForm.status = baseDataService.populateSelectList($scope.productForm.status,$scope.productStatusSet);
        });
        baseDataService.getBaseData(PRODUCT_TYPE_URI).then(function(response){
            $scope.productTypeSet = response.data;
            $scope.productForm.prodType = baseDataService.populateSelectList($scope.productForm.prodType,$scope.productTypeSet);
        });
        baseDataService.getBaseData(TAXRULE_ALL_URI).then(function(response){
            $scope.taxRuleSet = response.data;
            $scope.productForm.taxRules = baseDataService.populateMultiSelectList($scope.productForm.taxRules,$scope.taxRuleSet);
        });
        baseDataService.getBaseData(UNOM_ALL_URI).then(function(response){
            $scope.unitOfMeasureSet = response.data;
            //$scope.suppUnitOfMeasure = baseDataService.populateSelectList($scope.suppUnitOfMeasure,$scope.unitOfMeasureSet);
            $scope.productForm.prceUnitOfMeasure = baseDataService.populateSelectList($scope.productForm.prceUnitOfMeasure,$scope.unitOfMeasureSet);
        });
        baseDataService.getBaseData(PRGP_GET_ALL_TREEVIEW_URI).then(function(response){
            $scope.departmentSet = response.data;
            var newNode = { name: "<<N/A>>", id : -1 };
            for (j=0; j<$scope.departmentSet.length; j++) {
                for (i = 0; i < $scope.departmentSet[j].children.length; i++) {
                    if ($scope.departmentSet[j].children[i].children != undefined) {
                        $scope.departmentSet[j].children[i].children.unshift(newNode);
                    }
                }
            }
            if ($scope.department === undefined) {
                $scope.department = $scope.departmentSet[0];
            }
            $scope.department = baseDataService.populateSelectList($scope.department,$scope.departmentSet);
            $scope.changeDepartment();
        });
    }
    $scope.changeDepartment = function() {
        //populate category name
        if ($scope.department.children[0] != undefined) {
            $scope.category1Label = $scope.department.children[0].name;
            $scope.category1Set = $scope.department.children[0].children;
            $scope.category1 = { name: "<<N/A>>", id : getCategoryValueForDeptIdAndCatId($scope.department.id, $scope.department.children[0].id) };
            $scope.category1 = baseDataService.populateSelectList($scope.category1, $scope.category1Set);
        }
        if ($scope.department.children[1] != undefined) {
            $scope.category2Label = $scope.department.children[1].name;
            $scope.category2Set = $scope.department.children[1].children;
            $scope.category2 = { name: "<<N/A>>", id : getCategoryValueForDeptIdAndCatId($scope.department.id, $scope.department.children[1].id) };
            $scope.category2 = baseDataService.populateSelectList($scope.category2,$scope.category2Set);
        }
        if ($scope.department.children[2] != undefined) {
            $scope.category3Label = $scope.department.children[2].name;
            $scope.category3Set = $scope.department.children[2].children;
            $scope.category3 = { name: "<<N/A>>", id : getCategoryValueForDeptIdAndCatId($scope.department.id, $scope.department.children[2].id) };
            $scope.category3 = baseDataService.populateSelectList($scope.category3,$scope.category3Set);
        }
    }
    function getCategoryValueForDeptIdAndCatId(deptId, catId) {
        if ($scope.productForm.productGroups != undefined) {
            for (i=0; i<$scope.productForm.productGroups.length; i++) {
                if ($scope.productForm.productGroups[i].deptId == deptId && $scope.productForm.productGroups[i].catId == catId) {
                    return $scope.productForm.productGroups[i].catValId;
                }
            }
        }
        return -1;
    }
    function initSupplierPriceGrid() {
        $scope.gridOptions = {
            enableFiltering: true,
            enableCellEditOnFocus:true,
            expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" ui-grid-edit ui-grid-selection ui-grid-cellNav style ="height: 200px; width:50%;"></div>',
            expandableRowScope : {
                subGridVariable: 'subGridScopeVariable'
            },
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'solId', visible:false, enableCellEdit:false},
                {field:'prodId', visible:false, enableCellEdit:false},
                {field:'sprcPrefferBuy', displayName:'Prefer to buy',enableCellEdit:true, enableFiltering:false, type:'boolean', width:'10%',cellFilter:'booleanFilter', cellTemplate:'<input type="checkbox" ng-change="grid.appScope.selectDefaultSupplier(row.entity)" ng-model="row.entity.sprcPrefferBuy">',
                    cellClass:
                        function(grid, row, col, rowRenderIndex, colRenderIndex) {
                            if (grid.getCellValue(row, col) === true) {
                                return 'green';
                            } else {
                                return 'amber'
                            }
                        }
                },
                {field:'supplier.supplierName', displayName:'Supplier',enableCellEdit:false, enableFiltering:false, width:'25%',
                    cellTooltip: function(row,col) {
                        return row.entity.supplier.supplierName
                    }
                },
                {field:'catalogueNo', displayName:'CatNo',enableCellEdit:false,enableFiltering:false, width:'20%',
                    cellTooltip: function(row,col) {
                        return row.entity.catalogueNo
                    }
                },
                /*
                 {field:'partNo', enableCellEdit:false, width:'15%',
                 cellTooltip: function(row,col) {
                 return row.entity.partNo
                 }
                 },
                 */
                {field:'unitOfMeasure.unomCode', displayName:'Size',enableCellEdit:false, enableFiltering:false, width:'5%'},
                //{field:'unomQty',displayName:'Qty', enableCellEdit:true, type: 'number', width:'8%'},
                {field:'taxLegVariance.txlvDesc',editType:'dropdown', displayName:'Tax',enableCellEdit:true,width:'10%', enableFiltering:false,
                    editableCellTemplate:'<select class="form-control" data-ng-model="row.entity.taxLegVariance"  ng-options="tax.txlvDesc for tax in grid.appScope.taxLegVarianceSet" > </select>'
                    //cellTemplate:'<select class="form-control" data-ng-model="row.entity.taxLegVariance"  ng-options="tax.txlvDesc for tax in grid.appScope.taxLegVarianceSet" > </select>'
                },
                {field:'costBeforeTax', displayName:'cost(ex tax)',enableCellEdit:true, cellFilter: 'currency', width:'10%', enableFiltering:false},
                {field:'price', displayName:'RRP',enableCellEdit:true, cellFilter: 'currency', width:'10%', enableFiltering:false},
                {name:'Action',enableCellEdit:false,sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Remove" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-show="row.entity.id < 0" ng-click="grid.appScope.removeSuppProdPrice(row)"></i></a>&nbsp;<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-show="row.entity.id > 0" ng-click="grid.appScope.editSuppProdPrice(row)"></i></a>', width:'10%' }

            ]
        }
        $scope.gridOptions.enableRowSelection = false;
        $scope.gridOptions.multiSelect = false;

        //
        $scope.gridOptions.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            /*
             gridApi.selection.on.rowSelectionChanged($scope, function(row) {
             //baseDataService.setRow(row.entity);
             });
             */
            gridApi.cellNav.on.navigate($scope, function(newRowCol, oldRowCol){
            });
        };

        $scope.$on('uiGridEventEndCellEdit', function (event) {
            var suppProdPrice = event.targetScope.row.entity;
            if (event.targetScope.col.field == 'costBeforeTax') {
                suppProdPrice['price'] = suppProdPrice.costBeforeTax * (1 + suppProdPrice.taxLegVariance.txlvRate);
            }
            if (event.targetScope.col.field == 'price') {
                suppProdPrice['costBeforeTax'] = suppProdPrice.price * ( 1 - suppProdPrice.taxLegVariance.txlvRate);
            }
            $scope.selectDefaultSupplier(suppProdPrice);
        });


        if (!$scope.isNewPage) {
            $scope.gridOptions.data = $scope.productForm.suppProdPrices;
            for (i=0; i<$scope.gridOptions.data.length; i++) {
                displayBulkPrices($scope.gridOptions.data[i]);
            }
        }
    }
    $scope.addSuppProdPrice = function () {
        ngDialog.openConfirm({
            template:'views/pages/productSupplier.html',
            controller:'addProductSupplierCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {productSupplierObject: function(){return {}}}
        }).then (function (updatedProductSupplier){
                if (updatedProductSupplier != undefined) {
                    $scope.addSuppProdPriceToGrid(updatedProductSupplier);
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }

    $scope.editSuppProdPrice = function (row) {
        var suppProdPrice = row.entity;
        var productSupplier = {
            "supplier":suppProdPrice.supplier,
            "suppCatalogueNo" : suppProdPrice.catalogueNo,
            "suppPartNo" : suppProdPrice.partNo,
            "suppUnitOfMeasure" : suppProdPrice.unitOfMeasure,
            "suppUnomQty" : suppProdPrice.unomQty,
            "suppPrice" : suppProdPrice.price,
            "suppBulkQty" : suppProdPrice.bulkQty,
            "suppBulkQty2" : suppProdPrice.bulkQty2,
            "suppBulkQty3" : suppProdPrice.bulkQty3,
            "suppBulkQty4" : suppProdPrice.bulkQty4,
            "suppBulkQty5" : suppProdPrice.bulkQty5,
            "taxLegVariance" : suppProdPrice.taxLegVariance,
            "costBeforeTax" : suppProdPrice.costBeforeTax,
            "bulkPriceBeforeTax" : suppProdPrice.bulkPriceBeforeTax,
            "suppBulkPrice" : suppProdPrice.bulkPrice,
            "suppBulkPrice2" : suppProdPrice.bulkPrice2,
            "suppBulkPrice3" : suppProdPrice.bulkPrice3,
            "suppBulkPrice4" : suppProdPrice.bulkPrice4,
            "suppBulkPrice5" : suppProdPrice.bulkPrice5
        };

        ngDialog.openConfirm({
            template:'views/pages/productSupplier.html',
            controller:'addProductSupplierCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {productSupplierObject: function(){return productSupplier}}
        }).then (function (updatedProductSupplier){
                if (updatedProductSupplier != undefined) {
                    suppProdPrice.supplier = updatedProductSupplier.supplier;
                    suppProdPrice.catalogueNo = updatedProductSupplier.suppCatalogueNo;
                    suppProdPrice.partNo = updatedProductSupplier.suppPartNo;
                    suppProdPrice.unitOfMeasure = updatedProductSupplier.suppUnitOfMeasure;
                    suppProdPrice.unomQty = updatedProductSupplier.suppUnomQty;
                    suppProdPrice.price = updatedProductSupplier.suppPrice;
                    suppProdPrice.bulkQty = updatedProductSupplier.suppBulkQty;
                    suppProdPrice.bulkQty2 = updatedProductSupplier.suppBulkQty2;
                    suppProdPrice.bulkQty3 = updatedProductSupplier.suppBulkQty3;
                    suppProdPrice.bulkQty4 = updatedProductSupplier.suppBulkQty4;
                    suppProdPrice.bulkQty5 = updatedProductSupplier.suppBulkQty5;
                    suppProdPrice.taxLegVariance = updatedProductSupplier.taxLegVariance;
                    suppProdPrice.costBeforeTax = updatedProductSupplier.costBeforeTax;
                    suppProdPrice.bulkPriceBeforeTax = updatedProductSupplier.bulkPriceBeforeTax;
                    suppProdPrice.bulkPrice = updatedProductSupplier.suppBulkPrice;
                    suppProdPrice.bulkPrice2 = updatedProductSupplier.suppBulkPrice2;
                    suppProdPrice.bulkPrice3 = updatedProductSupplier.suppBulkPrice3;
                    suppProdPrice.bulkPrice4 = updatedProductSupplier.suppBulkPrice4;
                    suppProdPrice.bulkPrice5 = updatedProductSupplier.suppBulkPrice5;
                    displayBulkPrices(suppProdPrice);
                    $scope.selectDefaultSupplier(suppProdPrice);
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.addSuppProdPriceToGrid= function(productSupplier) {
        if (productSupplier.supplier == undefined || productSupplier.supplier == null) {
            alert('undefined');
            return
        }
        if (checkIfSuppProdExists($scope.gridOptions.data,productSupplier.supplier,productSupplier.suppCatalogueNo) > -1) {
            return;
        }
        var rowId;
        if ($scope.gridOptions.data == undefined && $scope.gridOptions.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.gridOptions.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        suppProdPrice = {
            "id" : rowId,
            "solId" : -1,
            "prodId" : -1,
            "supplier":productSupplier.supplier,
            "catalogueNo" : productSupplier.suppCatalogueNo,
            "partNo" : productSupplier.suppPartNo,
            "unitOfMeasure" : productSupplier.suppUnitOfMeasure,
            "unomQty" : productSupplier.suppUnomQty,
            "price" : productSupplier.suppPrice,
            "bulkQty" : productSupplier.suppBulkQty,
            "bulkQty2" : productSupplier.suppBulkQty2,
            "bulkQty3" : productSupplier.suppBulkQty3,
            "bulkQty4" : productSupplier.suppBulkQty4,
            "bulkQty5" : productSupplier.suppBulkQty5,
            "taxLegVariance" : productSupplier.taxLegVariance,
            "costBeforeTax" : productSupplier.costBeforeTax,
            "bulkPriceBeforeTax" : productSupplier.bulkPriceBeforeTax,
            "bulkPrice" : productSupplier.suppBulkPrice,
            "bulkPrice2" : productSupplier.suppBulkPrice2,
            "bulkPrice3" : productSupplier.suppBulkPrice3,
            "bulkPrice4" : productSupplier.suppBulkPrice4,
            "bulkPrice5" : productSupplier.suppBulkPrice5
        }
        displayBulkPrices(suppProdPrice);
        $scope.gridOptions.data.push(suppProdPrice);
        $scope.selectDefaultSupplier(suppProdPrice);
    };
    function displayBulkPrices(productSupplier) {
        productSupplier.subGridOptions = {
            enableRowSelection :false,
            enableCellEditOnFocus:true,
            enableColumnResizing: true,
            columnDefs :[
                {field:"name",visible:false},
                {field:"qty", displayName:'Bulk Qty',enableCellEdit:true, type: 'number', width:'20%', visible:true},
                {field:"value", displayName:'Bulk Price',enableCellEdit:true, cellFilter: 'currency', width:'20%', visible:true}
            ],
            data:[]
        }
        if (productSupplier.bulkQty !=undefined && productSupplier.bulkPrice != undefined) {
            console.log('bulk1 processing');
            bulkPrice =  {
                "name" : "bulk1",
                "qty" : productSupplier.bulkQty,
                "value" : productSupplier.bulkPrice
            }
            productSupplier.subGridOptions.data.push(bulkPrice);
        }
        if (productSupplier.bulkQty2 !=undefined && productSupplier.bulkPrice2 != undefined) {
            bulkPrice ={
                "name" : "bulk2",
                "qty" : productSupplier.bulkQty2,
                "value" : productSupplier.bulkPrice2
            }
            productSupplier.subGridOptions.data.push(bulkPrice);
        }
        if (productSupplier.bulkQty3 !=undefined && productSupplier.bulkPrice3 != undefined) {
            bulkPrice ={
                "name" : "bulk3",
                "qty" : productSupplier.bulkQty3,
                "value" : productSupplier.bulkPrice3
            }
            productSupplier.subGridOptions.data.push(bulkPrice);
        }
        if (productSupplier.bulkQty4 !=undefined && productSupplier.bulkPrice4 != undefined) {
            bulkPrice ={
                "name" : "bulk4",
                "qty" : productSupplier.bulkQty4,
                "value" : productSupplier.bulkPrice4
            }
            productSupplier.subGridOptions.data.push(bulkPrice);
        }
        if (productSupplier.bulkQty5 !=undefined && productSupplier.bulkPrice5 != undefined) {
            bulkPrice ={
                "name" : "bulk5",
                "qty" : productSupplier.bulkQty5,
                "value" : productSupplier.bulkPrice5
            }
            productSupplier.subGridOptions.data.push(bulkPrice);
        }
    }

    /**
     * save bulkprice and delete subGridOptions.
     */
    function saveBulkPrices() {
        for (i=0; i<$scope.gridOptions.data.length; i++) {
            productSupplier = $scope.gridOptions.data[i];
            if (productSupplier.subGridOptions != undefined && productSupplier.subGridOptions.data != undefined) {
                for (j=0; j<productSupplier.subGridOptions.data.length; j++) {
                    bulkPrice = productSupplier.subGridOptions.data[j];
                    if (bulkPrice == undefined || bulkPrice == null) {
                        continue;
                    }
                    switch (bulkPrice.name) {
                        case 'bulk1' :
                            productSupplier.bulkPrice = bulkPrice.value;
                            productSupplier.bulkQty = bulkPrice.qty;
                        case 'bulk2' :
                            productSupplier.bulkPrice2 = bulkPrice.value;
                            productSupplier.bulkQty2 = bulkPrice.qty;
                        case 'bulk3' :
                            productSupplier.bulkPrice3 = bulkPrice.value;
                            productSupplier.bulkQty3 = bulkPrice.qty;
                        case 'bulk4' :
                            productSupplier.bulkPrice4 = bulkPrice.value;
                            productSupplier.bulkQty4 = bulkPrice.qty;
                        case 'bulk5' :
                            productSupplier.bulkPrice5 = bulkPrice.value;
                            productSupplier.bulkQty5 = bulkPrice.qty;
                        default:
                    }

                }
            }
            delete productSupplier.subGridOptions;
        }

    }

    $scope.removeSuppProdPrice = function(row) {
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

    function checkIfSuppProdExists(arr,supplier, catalogNo) {
        if (arr == undefined || supplier== undefined || catalogNo==undefined)
            return -1;
        for (var j = 0; j < arr.length; j++) {
            if ( (arr[j].supplier.supplierCode === supplier.supplierCode)&&(arr[j].catalogueNo === catalogNo)) {
                return j;
            }
        }
        return -1;
    };


    //create new product
    $scope.createProduct = function () {

        if (!$scope.isNewPage) {
            submitForm();
            return;
        }
        //if new product:
        //check if product already exists.
        $scope.checkIfProductExists().then(function (result) {
            if (result) {
                baseDataService.displayMessage('yesNo', 'Warnning', 'Product with SKU: ' + $scope.productForm.prodSku + ' And Ref: ' + $scope.productForm.reference + ' Already exists. Do you want to continue add a new product?').then(function (result) {
                    if (result) {
                        submitForm();
                    } else {
                        return;
                    }
                });
            } else {
                submitForm();
            }
        });
    };

    function buildProdDeptCat(inDeptId, inCatId, inCatValId) {
        prodDeptCat = {
            deptId: inDeptId,
            catId : inCatId,
            catValId: inCatValId
        };
        return prodDeptCat;
    }

    function submitForm() {
        saveBulkPrices();
        $scope.productForm.suppProdPrices = $scope.gridOptions.data;
        //update product groups
        var productGroupList = [];
        if ($scope.category1 != undefined) {
            productGroupList.push(buildProdDeptCat($scope.department.id, $scope.department.children[0].id, $scope.category1.id ));
        }
        if ($scope.category2 != undefined) {
            productGroupList.push(buildProdDeptCat($scope.department.id, $scope.department.children[1].id, $scope.category2.id ));
        }
        if ($scope.category3 != undefined) {
            productGroupList.push(buildProdDeptCat($scope.department.id, $scope.department.children[2].id, $scope.category3.id ));
        }
        $scope.productForm.productGroups = productGroupList;

        var rowObject = $scope.productForm;
        baseDataService.addRow(rowObject, PRODUCT_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listProduct');
            } else {
                alert('Not able to save Product. ' + addResponse.message);
            }
        });
    }

    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    };

    $scope.displayProductPurchaseOrders = function () {
        if ($scope.isNewPage) {
            return;
        }
        ngDialog.openConfirm({
            template:'views/pages/productPurchaseOrderList.html',
            controller:'productPurchaseOrderListCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {productId: function(){return $scope.productForm.prodId}}
        }).then (function (){
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.displayProductTxns = function (transactionType) {
        if ($scope.isNewPage) {
            return;
        }
        ngDialog.openConfirm({
            template:'views/pages/productTxnList.html',
            controller:'productTxnListCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {productId: function(){return $scope.productForm.prodId}, txnType: function(){return transactionType}}
        }).then (function (){
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    };

    $scope.selectDefaultSupplier = function(row) {
        console.log('selectDefaultSupplier called');
        var selectedCount = 0;
        if (row.sprcPrefferBuy) {
            for (i=0; i<$scope.gridOptions.data.length; i++) {
                if ($scope.gridOptions.data[i].sprcPrefferBuy) {
                    selectedCount ++;
                }
            }
            if (selectedCount > 1) {
                baseDataService.displayMessage('info','Warning', 'You can only have one default supplier');
                row.sprcPrefferBuy = false;
                return;
            }
            $scope.productForm.prceUnitOfMeasure = baseDataService.populateSelectList(row.unitOfMeasure,$scope.unitOfMeasureSet);
            $scope.productForm.prceUnomQty = row.unomQty;
            $scope.productForm.costPrice = row.costBeforeTax;
            $scope.productForm.prcePrice = row.price;
        }
        //set the product cost and other items per default supplier
        //$scope.productForm.prceUnitOfMeasure = row.unitOfMeasure;
    };
    $scope.displayProductAuditTrail = function () {
        if ($scope.isNewPage) {
            return;
        }
        ngDialog.openConfirm({
            template:'views/pages/generalPopUpList.html',
            controller:'productAuditTrailCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {searchUrl: function(){return PRODUCT_AUDIT_TRAIL_URI + $scope.productForm.prodId}}
        }).then (function (selectedItem){
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    $scope.checkIfProductExists = function() {
        var checkProductUrl = PRODUCT_CHECK_EXISTENCE_URI + $scope.productForm.prodSku + '/' + $scope.productForm.reference;
        var promise = baseDataService.getBaseData(checkProductUrl).then(function(response){
            var result = response.data;
            if (result.status === SUCCESS) {
                return false;
            } else {
                return true;
            }
        });
        return promise;
    }
});
