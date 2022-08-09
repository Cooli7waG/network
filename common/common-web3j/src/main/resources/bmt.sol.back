// SPDX-License-Identifier: MIT

pragma solidity >=0.7.0 <0.9.0;


contract BMT_ERC20 {

    mapping(address => uint256) private _balances;

    mapping(address => mapping(address => uint256)) private _allowances;

    uint256 private _totalSupply;
    string private _name;
    string private _symbol;

    address public _owner;
    address public _foundations;

    uint8 private constant _decimals = 18;

    event Transfer(address indexed from, address indexed to, uint256 value);
    event Approval(address indexed owner, address indexed spender, uint256 value);
    //event NotEnoughAwardToken(address indexed recipient, uint256 value);


    /* modifier */
    modifier onlyOwner(){
        require(msg.sender == _owner, "only owner could do this!");
        _;
    }

    modifier onlyFoundations(){
        require(msg.sender == _foundations, "noly foundations could do this!");
        _;
    }


    constructor(string memory name_, string memory symbol_, uint256 totalSupply_) {
        _name = name_;
        _symbol = symbol_;
        _totalSupply = totalSupply_ * (10 ** _decimals);
        _owner = msg.sender;
        _balances[_owner] = _totalSupply;
    }


    function name() public view   returns (string memory) {
        return _name;
    }

    function symbol() public view   returns (string memory) {
        return _symbol;
    }


    function decimals() public pure   returns (uint8) {
        return _decimals;
    }


    function totalSupply() public view   returns (uint256) {
        return _totalSupply;
    }


    function balanceOf(address account) public view  returns (uint256) {
        return _balances[account];
    }


    function transfer(address to, uint256 amount) public  returns (bool) {
        //address owner = _msgSender();
        address owner = msg.sender;
        _transfer(owner, to, amount);
        return true;
    }

    function allowance(address owner, address spender) public view  returns (uint256) {
        return _allowances[owner][spender];
    }

    function approve(address spender, uint256 amount) public returns (bool) {
        //address owner = _msgSender();
        address owner = msg.sender;
        _approve(owner, spender, amount);
        return true;
    }

    function transferFrom(address from, address to, uint256 amount) public  returns (bool) {
        //address spender = _msgSender();
        address spender = msg.sender;
        _spendAllowance(from, spender, amount);
        _transfer(from, to, amount);
        return true;
    }

    function increaseAllowance(address spender, uint256 addedValue) public  returns (bool) {
        //address owner = _msgSender();
        address owner = msg.sender;
        _approve(owner, spender, allowance(owner, spender) + addedValue);
        return true;
    }

    function decreaseAllowance(address spender, uint256 subtractedValue) public  returns (bool) {
        //address owner = _msgSender();
        address owner = msg.sender;
        uint256 currentAllowance = allowance(owner, spender);
        require(currentAllowance >= subtractedValue, "ERC20: decreased allowance below zero");
        unchecked {
            _approve(owner, spender, currentAllowance - subtractedValue);
        }

        return true;
    }


    function initFoundations(address foundations, uint256 value) public onlyOwner{
        require(_foundations == address(0));
        require(value <= _balances[_owner]);
        _foundations = foundations;
        _transfer(_owner, foundations, value);
    }

    function cancelFoundations(address foundations) public onlyOwner{
        require(_foundations == foundations);
        _transfer(foundations, _owner, _balances[_foundations]);
        _foundations = address(0);
    }

    function increaseFoundationsToken(uint256 value) public onlyOwner{
        _transfer(_owner, _foundations, value);
    }

    function reduceFoundationsToken(uint256 value) public onlyOwner{
        _transfer(_foundations, _owner, value);
    }

    function replaceFoundations(address newFoundations) public onlyOwner{
        require(newFoundations != address(0));
        uint256 leftBalance = _balances[_foundations];
        _transfer(_foundations, newFoundations, leftBalance);
        _foundations = newFoundations;

    }


    function rewardMiner(address miner, uint256 value) public onlyFoundations {
        _transfer(_foundations, miner, value);        
    }

    function reduceMinerToken(address miner, uint256 value) public onlyFoundations{
        require(value <= _balances[miner]);
        _transfer(miner, _foundations, value);
    }

    function batchRewardMiners(address[] memory miners, uint256[] memory values) public onlyFoundations{
        require(miners.length == values.length);
        require(miners.length <= 20);

        for(uint256 i = 0; i<miners.length; i++){
            _transfer(_foundations, miners[i], values[i]);
        }
    }

    function getMultiBalances(address[] memory miners) public view onlyFoundations returns(uint256[] memory){
        require(miners.length <= 20);
        uint256[] memory balances = new uint256[](miners.length); 
        for(uint256 i = 0; i<miners.length; i++){
            balances[i] = _balances[miners[i]];
        }

        return balances;
    }



    function _transfer(address from, address to, uint256 amount) internal  {
        require(from != address(0), "ERC20: transfer from the zero address");
        require(to != address(0), "ERC20: transfer to the zero address");

        //_beforeTokenTransfer(from, to, amount);

        uint256 fromBalance = _balances[from];
        require(fromBalance >= amount, "ERC20: transfer amount exceeds balance");
        unchecked {
            _balances[from] = fromBalance - amount;
        }
        _balances[to] += amount;

        emit Transfer(from, to, amount);

        //_afterTokenTransfer(from, to, amount);
    }

    function _approve(address owner, address spender, uint256 amount) internal  {
        require(owner != address(0), "ERC20: approve from the zero address");
        require(spender != address(0), "ERC20: approve to the zero address");

        _allowances[owner][spender] = amount;
        emit Approval(owner, spender, amount);
    }

    function _spendAllowance(address owner, address spender, uint256 amount) internal  {
        uint256 currentAllowance = allowance(owner, spender);
        if (currentAllowance != type(uint256).max) {
            require(currentAllowance >= amount, "ERC20: insufficient allowance");
            unchecked {
                _approve(owner, spender, currentAllowance - amount);
            }
        }
    }

    
}
