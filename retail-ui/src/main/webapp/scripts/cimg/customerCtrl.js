/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('customerCtrl', function($scope, $state, $stateParams,ngDialog,uiGridConstants,viewMode, UserService, baseDataService, SUCCESS, FAILURE, CUSTOMER_ADD_URI, CUSTOMERGRADE_ALL_URI, CUSTOMER_TYPE_URI, CUSTOMER_STATUS_URI, CUSTOMER_GET_ACCOUNT_PAYMENT_URI, CUSTOMER_ALL_INVOICE_URI, CUSTOMER_ALL_SALEORDER_URI,CUSTOMER_ALL_BOQ_URI, INVOICE_GET_URI, INVOICE_EXPORT_PDF, BOQ_GET_URI, TXN_EXPORT_PDF,ALL_CATEGORY_OFTYPE_URI, COUNTRY_LIST_URI ) {
    $scope.isNewPage = true;
    $scope.isViewMode = false;
    if (viewMode!=undefined) {
        $scope.isViewMode = viewMode;
    }
    initContactList();
    initCustomerDebtList();
    initCustomerInvoiceList();
    initCustomerSaleOrderList();
    initCustomerBoqList();
    initPageData();

    function initCustomerDebtList() {
        $scope.debtList = {
            enableFiltering: true,
            showGridFooter: true,
            showColumnFooter: true,
            enableRowSelection:false,
            columnDefs: [
                {field: 'id', visible: false, enableCellEdit: false},
                {field:'cadPaid', displayName:'Paid', cellFilter:'booleanFilter',
                    cellClass:
                        function(grid, row, col, rowRenderIndex, colRenderIndex) {
                            if (grid.getCellValue(row, col) === true) {
                                return 'green';
                            } else {
                                return 'red'
                            }
                        },
                    enableCellEdit:false, width:'5%'
                },
                {field: 'cadStartDate', displayName:'Start Date',enableCellEdit:false, width:'12.5%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field: 'cadDueDate', displayName:'Due Date',enableCellEdit:false, width:'12.5%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field: 'cadPaymentDate', displayName:'Paid Date',enableCellEdit:false, width:'12.5%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field: 'txhdTxnNr', displayName: 'Invoice No.', width: '20%'},
                {field: 'cadAmountDebt', displayName: 'Debt Amount', enableCellEdit: false, cellFilter: 'currency', width: '20%', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
                {field: 'balance', displayName: 'Balance', enableCellEdit: false, cellFilter: 'currency', width: '17.5%', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum}
            ]
        }
        $scope.debtList.enableRowSelection = false;
        $scope.debtList.multiSelect = false;
        $scope.debtList.onRegisterApi = function (gridApi) {
            $scope.debtListGridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                baseDataService.setRow(row.entity);
            });

        };
    }


    function initCustomerInvoiceList() {
        $scope.invoiceList = {
            enableFiltering: true,
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'20%'},
                {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field:'txhdOrigTxnNr', displayName:'Sale Order No',enableCellEdit:false, width:'15%'},
                {field:'txhdTxnNr', displayName:'Invoice No',enableCellEdit:false, width:'15%'},
                {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'15%', cellFilter:'currency'},
                {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'15%', cellFilter:'currency'},
                {name:'Action', cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editTransaction(row)"></i></a>&nbsp;<a href=""><i tooltip="Print" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.exportToPdf(row)"></i></a>', width:'10%' }
            ]
        }
        $scope.invoiceList.enableRowSelection = true;
        $scope.invoiceList.multiSelect = false;
        $scope.invoiceList.noUnselect= true;

        //
        $scope.invoiceList.onRegisterApi = function (gridApi) {
            $scope.invoiceListApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                baseDataService.setRow(row.entity);
            });
        };
    }

    function initCustomerSaleOrderList() {
        $scope.saleOrderList = {
            enableFiltering: true,
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'20%'},
                {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field:'txhdTxnNr', displayName:'Number',enableCellEdit:false, width:'15%'},
                {field:'status', displayName:'Status', enableCellEdit:false, width:'15%', cellFilter:'configCategoryFilter',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return grid.getCellValue(row, col).color
                    }
                },
                {field:'txhdTxnType.displayName' , displayName:'Type', enableCellEdit:false, width:'10%'},
                {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'10%', cellFilter:'currency'},
                {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'10%', cellFilter:'currency'},
                {name:'Action', cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editSaleOrder(row)"></i></a>&nbsp;<a href=""><i tooltip="Print" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.printSaleOrder(row)"></i></a>', width:'10%' }
            ]
        }
        $scope.saleOrderList.enableRowSelection = true;
        $scope.saleOrderList.multiSelect = false;
        $scope.saleOrderList.noUnselect= true;

        //
        $scope.saleOrderList.onRegisterApi = function (gridApi) {
            $scope.saleOrderListApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                baseDataService.setRow(row.entity);
            });
        };

    }

    function initCustomerBoqList() {
        $scope.boqList = {
            enableFiltering: true,
            enableSelectAll:false,
            enableRowSelection:true,
            showGridFooter: true,
            showColumnFooter: true,
            enableColumnResizing: true,
            enableSorting:true,
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'project.projectCode', displayName:'Project No',enableCellEdit:false, width:'15%',
                    cellTooltip: function(row,col) {
                        return row.entity.project.projectCode
                    }
                },
                {field:'orderNo', displayName:'Client Order',enableCellEdit:false, width:'10%'},
                {field:'project.projectName',displayName:'Project', enableCellEdit:false, width:'35%',
                    cellTooltip: function(row,col) {
                        return row.entity.project.projectName
                    }
                },
                {field:'project.referenceNo', displayName:'Project Ref.',enableCellEdit:false, width:'10%', visible:false,
                    cellTooltip: function(row,col) {
                        return row.entity.project.referenceNo
                    }
                },
                {field:'dateCreated', displayName:'Created',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\'' },
                {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
                //{field:'boqValueGross', displayName:'Gross Value',enableCellEdit:false, width:'7%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
                //{field:'boqValueNett', displayName:'Net Value',enableCellEdit:false, width:'8%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
                {field:'boqStatus', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return grid.getCellValue(row, col).color
                    }
                },
                {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="View Detail" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.viewBoqDetail(row)"></i></a>', width:'5%' }
            ]
        }
        $scope.boqList.enableRowSelection = true;
        $scope.boqList.multiSelect = true;
        //$scope.boqList.noUnselect= true;

        //
        $scope.boqList.onRegisterApi = function (gridApi) {
            $scope.boqListApi = gridApi;
        };

    }



    //set default data on the page
    function initContactList() {
        $scope.gridOptions = {
            enableFiltering: true,
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'contactType', displayName:'Position',enableCellEdit:false, width:'15%', cellFilter:'configCategoryFilter',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return grid.getCellValue(row, col).color
                    }
                },
                {field:'title', displayName:'Title', enableCellEdit:false, width:'5%'},
                {field:'firstName', displayName:'First Name',enableCellEdit:false, width:'20%',
                    cellTooltip: function(row,col) {
                        return row.entity.firstName
                    }
                },
                {field:'surName', displayName:'Sur Name',enableCellEdit:false, width:'20%',
                    cellTooltip: function(row,col) {
                        return row.entity.surName
                    }
                },
                {field:'mobile', enableCellEdit:false, width:'10%'},
                {field:'email', enableCellEdit:false, width:'20%'},
                {name:'Action', cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editContact(row)"></i></a><a href=""><i tooltip="Remove" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-click="grid.appScope.removeContact(row)"></i></a>', width:'5%' }
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
    }

    function initPageData() {
        if ( $stateParams.blankPage) {
            $scope.customer = {};
            $scope.customer.creditStartEom = false;
            $scope.customer.creditStartEom = false;
            $scope.customer.creditStartDate= getLastDateOfMonth();
            $scope.customer.contact = {};
            $scope.isNewPage = true;
        } else {
            $scope.isNewPage = false;
            $scope.customer = angular.copy(baseDataService.getRow());
            $scope.gridOptions.data = $scope.customer.contacts;
            $scope.customer.creditStartDate = new Date($scope.customer.creditStartDate);
            if ($scope.customer.contact === null || $scope.customer.contact === undefined) {
                $scope.customer.contact = {};
            }
            //baseDataService.setIsPageNew(true);
            //baseDataService.setRow({});
            initCustomerRelatedData($scope.customer);
        }
        baseDataService.getBaseData(CUSTOMERGRADE_ALL_URI).then(function(response){
            $scope.gradeSet = response.data;
            $scope.customer.grade = baseDataService.populateSelectList($scope.customer.grade,$scope.gradeSet);
        });
        baseDataService.getBaseData(CUSTOMER_TYPE_URI).then(function(response){
            $scope.customerTypeSet = response.data;
            $scope.customer.customerType = baseDataService.populateSelectList($scope.customer.customerType,$scope.customerTypeSet);
        });
        baseDataService.getBaseData(CUSTOMER_STATUS_URI).then(function(response){
            $scope.customerStatusSet = response.data;
            $scope.customer.customerStatus = baseDataService.populateSelectList($scope.customer.customerStatus, $scope.customerStatusSet);
        });
        baseDataService.getBaseData(COUNTRY_LIST_URI).then(function(response){
            $scope.countrySet = response.data;
            $scope.country = baseDataService.populateCategoryPerDisplayName($scope.customer.contact.country,$scope.countrySet);
            $scope.customer.contact.country = $scope.country.displayName;
            populateStateList();
        });
    }
    $scope.onCountryChange = function () {
        $scope.customer.contact.country = $scope.country.displayName;
        populateStateList();
    };

    $scope.onStateChange = function () {
        $scope.customer.contact.state = $scope.state.displayName;
    };

    function populateStateList() {
        baseDataService.getBaseData(ALL_CATEGORY_OFTYPE_URI + $scope.country.categoryCode).then(function(response){
            $scope.stateSet = response.data;
            $scope.state = baseDataService.populateCategoryPerDisplayName($scope.customer.contact.state,$scope.stateSet);
        });
    }

    //create new customer
    $scope.createCustomer = function () {
        //$scope.facility.lastModifiedBy = userId;
        $scope.customer.contacts = $scope.gridOptions.data;
        if ($scope.state !== undefined) {
            $scope.customer.contact.state = $scope.state.displayName;
        }
        if ($scope.country !== undefined) {
            $scope.customer.contact.country = $scope.country.displayName;
        }

        var rowObject = $scope.customer;
        baseDataService.addRow(rowObject, CUSTOMER_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listCustomer');
            } else {
                alert('Not able to save customer. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }

    $scope.addContact = function () {
        var rowId;
        if ($scope.gridOptions.data == undefined && $scope.gridOptions.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.gridOptions.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        ngDialog.openConfirm({
            template:'views/pages/contact.html',
            controller:'addContactCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {contactObject: function(){return {}}}
        }).then (function (updatedContact){
                if (updatedContact != undefined) {
                    //row = updatedContact;
                    updatedContact.id = rowId;
                    $scope.gridOptions.data.push(updatedContact);
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }
    $scope.editContact = function (row) {
        var contactBackUp = angular.copy(row.entity);
        var contactObject = row.entity;

        ngDialog.openConfirm({
            template:'views/pages/contact.html',
            controller:'addContactCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {contactObject: function(){return contactObject}}
        }).then (function (updatedContact){
                if (updatedContact != undefined) {
                    row.entity = updatedContact;
                }
            }, function(reason) {
                row.entity = contactBackUp;
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }
    $scope.removeContact = function(row) {
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete this contact?').then(function(result){
            if (result) {
                if (row == undefined || row.entity == undefined) {
                    alert('item is undefined');
                    return;
                }
                var rowIndex = baseDataService.getArrIndexOf($scope.gridOptions.data, row.entity);
                if (rowIndex>-1) {
                    $scope.gridOptions.data.splice(rowIndex,1);
                }
            } else {
                return;
            }
        });

    }

    function getLastDateOfMonth() {
        var date = new Date();
        //var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
        var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
        return lastDay;
    }
    $scope.setCreditStartEom = function() {
        $scope.customer.creditStartDate = getLastDateOfMonth();
    }

    $scope.onCreditLimitChange= function() {
        if ($scope.customer.owing == undefined) {
            $scope.customer.owing = 0;
        }
        $scope.customer.remainCredit = $scope.customer.creditLimit*1 - $scope.customer.owing*1;
    }


    $scope.editTransaction = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var txnSaleGetURI = INVOICE_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.createSaleTransaction');
        });
    }
    $scope.exportToPdf = function(row) {

        var exportUrl = INVOICE_EXPORT_PDF + row.entity.id;
        baseDataService.pdfViewer(exportUrl);
    }

    $scope.editSaleOrder = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var txnSaleGetURI = TXN_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.createSaleTransaction');
        });
    }

    $scope.printSaleOrder = function(row) {

        var exportUrl = TXN_EXPORT_PDF + row.entity.id;
        baseDataService.pdfViewer(exportUrl);
        /*
        baseDataService.getStreamData(exportUrl).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            var myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            baseDataService.setPdfContent(myPdfContent);
            $state.go('dashboard.pdfViewer');
        });
        */
    }



    function initCustomerRelatedData(customer) {
        baseDataService.getBaseData(CUSTOMER_GET_ACCOUNT_PAYMENT_URI + customer.id).then(function(response){
            $scope.debtList.data = response.data;
        });

        baseDataService.getBaseData(CUSTOMER_ALL_INVOICE_URI + customer.id).then(function(response){
            $scope.invoiceList.data = response.data;
        });

        baseDataService.getBaseData(CUSTOMER_ALL_SALEORDER_URI + customer.id).then(function(response){
            $scope.saleOrderList.data = response.data;
        });

        baseDataService.getBaseData(CUSTOMER_ALL_BOQ_URI + customer.id).then(function(response){
            $scope.boqList.data = response.data;
        });

    }

    $scope.viewBoqDetail = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var boqGetURI = BOQ_GET_URI +  row.entity.id;
        baseDataService.getBaseData(boqGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.viewBoqDetail');
        });
    };
    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    };

    $scope.checkAccessToAccountDetail = function() {
        return UserService.checkUserHasAccess('viewCustomerAccountDetail');
    }

});

