/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('customerCtrl', function($scope, $state,ngDialog,uiGridConstants, UserService, baseDataService, SUCCESS, FAILURE, CUSTOMER_ADD_URI, CUSTOMERGRADE_ALL_URI, CUSTOMER_TYPE_URI, CUSTOMER_STATUS_URI, CUSTOMER_GET_ACCOUNT_DEBT_URI, CUSTOMER_ALL_INVOICE_URI, CUSTOMER_ALL_SALEORDER_URI, INVOICE_GET_URI, INVOICE_EXPORT_PDF ) {

    initContactList();
    initCustomerDebtList();
    initCustomerInvoiceList();
    initCustomerSaleOrderList();
    initPageData();

    function initCustomerDebtList() {
        $scope.debtList = {
            enableFiltering: true,
            showGridFooter: true,
            showColumnFooter: true,
            enableRowSelection:false,
            columnDefs: [
                {field: 'id', visible: false, enableCellEdit: false},
                {field: 'cadStartDate', displayName:'Start Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'yyyy-MM-dd HH:mm\''},
                {field: 'cadDueDate', displayName:'Due Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'yyyy-MM-dd HH:mm\''},
                {field: 'txhdTxnNr', displayName: 'Invoice No.', width: '15%'},
                {field: 'cadAmountDebt', displayName: 'Amount Owing', enableCellEdit: false, cellFilter: 'currency', width: '10%', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
                {field: 'paying', displayName: 'Paying', enableCellEdit: false, cellFilter: 'currency', width: '10%', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
                {field: 'balance', displayName: 'Balance', enableCellEdit: false, cellFilter: 'currency', width: '10%',footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum}
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
                {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'yyyy-MM-dd HH:mm\''},
                {field:'txhdOrigTxnNr', displayName:'Sale Order No',enableCellEdit:false, width:'15%'},
                {field:'txhdTxnNr', displayName:'Invoice No',enableCellEdit:false, width:'15%'},
                {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'15%', cellFilter:'currency'},
                {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'15%', cellFilter:'currency'},
                {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editTransaction(row)"></i></a>&nbsp;<a href=""><i tooltip="Print" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.exportToPdf(row)"></i></a>', width:'10%' }
            ]
        }
        $scope.invoiceList.enableRowSelection = true;
        $scope.invoiceList.multiSelect = false;
        $scope.invoiceList.noUnselect= true;

        //
        $scope.invoiceList.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
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
                {field:'txhdTradingDate', displayName:'Create Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'yyyy-MM-dd HH:mm\''},
                {field:'txhdTxnNr', displayName:'Number',enableCellEdit:false, width:'15%'},
                {field:'txhdState', displayName:'State', enableCellEdit:false, width:'15%', cellFilter:'configCategoryFilter',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return grid.getCellValue(row, col).color
                    }
                },
                {field:'txhdTxnType.displayName' , displayName:'Type', enableCellEdit:false, width:'10%'},
                {field:'txhdValueNett', displayName:'Total',enableCellEdit:false, width:'10%', cellFilter:'currency'},
                {field:'txhdValueDue', displayName:'Due',enableCellEdit:false, width:'10%', cellFilter:'currency'},
                {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editSaleOrder(row)"></i></a>&nbsp;<a href=""><i tooltip="Print" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.printSaleOrder(row)"></i></a>', width:'10%' }
            ]
        }
        $scope.saleOrderList.enableRowSelection = true;
        $scope.saleOrderList.multiSelect = false;
        $scope.saleOrderList.noUnselect= true;

        //
        $scope.saleOrderList.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                baseDataService.setRow(row.entity);
            });
        };

    }

    //set default data on the page
    function initContactList() {
        $scope.gridOptions = {
            enableFiltering: true,
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'contactType', displayName:'Type',enableCellEdit:false, width:'15%', cellFilter:'configCategoryFilter',
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
                {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editContact(row)"></i></a><a href=""><i tooltip="Remove" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-click="grid.appScope.removeContact(row)"></i></a>', width:'5%' }
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
        if ( baseDataService.getIsPageNew()) {
            $scope.customer = {};
            $scope.customer.creditStartEom = false;
            $scope.customer.creditStartEom = false;
            $scope.customer.creditStartDate= getLastDateOfMonth();
        } else {
            $scope.customer = angular.copy(baseDataService.getRow());
            $scope.gridOptions.data = $scope.customer.contacts;
            $scope.customer.creditStartDate = new Date($scope.customer.creditStartDate);
            baseDataService.setIsPageNew(true);
            baseDataService.setRow({});
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
    }

    //create new customer
    $scope.createCustomer = function () {
        //$scope.facility.lastModifiedBy = userId;
        $scope.customer.contacts = $scope.gridOptions.data;
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
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.createSaleTransaction');
        });
    }
    $scope.exportToPdf = function(row) {

        var exportUrl = INVOICE_EXPORT_PDF + row.entity.id;
        baseDataService.getStreamData(exportUrl).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            var myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            baseDataService.setPdfContent(myPdfContent);
            $state.go('dashboard.pdfViewer');
        });
    }

    $scope.editSaleOrder = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var txnSaleGetURI = TXN_GET_URI + row.entity.id;
        baseDataService.getBaseData(txnSaleGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.createSaleTransaction');
        });
    }

    $scope.printSaleOrder = function(row) {

        var exportUrl = TXN_EXPORT_PDF + row.entity.id;
        baseDataService.getStreamData(exportUrl).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            var myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            baseDataService.setPdfContent(myPdfContent);
            $state.go('dashboard.pdfViewer');
        });
    }



    function initCustomerRelatedData(customer) {
        baseDataService.getBaseData(CUSTOMER_GET_ACCOUNT_DEBT_URI + customer.id).then(function(response){
            $scope.debtList.data = response.data;
        });

        baseDataService.getBaseData(CUSTOMER_ALL_INVOICE_URI + customer.id).then(function(response){
            $scope.invoiceList.data = response.data;
        });

        baseDataService.getBaseData(CUSTOMER_ALL_SALEORDER_URI + customer.id).then(function(response){
            $scope.saleOrderList.data = response.data;
        });

    }
});
