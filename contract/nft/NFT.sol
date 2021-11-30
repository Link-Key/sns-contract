// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "./ERC721.sol";
import "./Ownable.sol";
import "./Address.sol";
import "./Strings.sol";
import "./IERC721.sol";
import "./IERC721Receiver.sol";
import "./IERC721Metadata.sol";
import "./Context.sol";
import "./ERC165.sol";
import "./IERC165.sol";
import "./ERC721Enumerable.sol";
import "./ERC721URIStorage.sol";
import "./SafeMath.sol";


contract NFT is ERC721URIStorage,Ownable{
    using Address for address;
    using SafeMath for uint256;

    //NFT总量
    uint256 private _totalSupply;

    //是否可续费
    bool private _canRenew;
    //是否支持增发
    //    bool private _canIssua;
    //是否支持核销
    bool private _canWriteOff;

    //会员到期时间
    mapping(uint256 => uint256) private _expirationTime;

    //初始有效期
    uint256 private _initialDeadline;

    //权益链接
    string private _equityLink;

    //已经铸造的数量
    uint256 private _tokenMinted;

    //批量铸造数量
    uint8 constant BATCH_NUM = 150;

    /**
     * @dev Initializes the contract by setting a `name` and a `symbol` to the token collection.
     */
    constructor(string memory name_, string memory symbol_, uint256  totalSupply_, string memory equityLink_, bool canRenew_, bool canWriteOff_,uint256 initialDeadline_) {
        _name = name_;
        _symbol = symbol_;

        _totalSupply = totalSupply_;
        _equityLink = equityLink_;

        _canRenew = canRenew_;
        //        _canIssua = canIssua_;
        _canWriteOff = canWriteOff_;

        require(initialDeadline_ != 0,"NFT.sol --- setInitialDeadline --- initialDeadline_ != 0!!!");
        _initialDeadline = initialDeadline_;

        _tokenMinted = 0;
    }

    // ------------------------- 事件 ----------------------------

    event Sell(address owner_,address to_,uint256 indexed tokenId_, uint256 expirationTime_);
    event Renew(address owner_,uint256 indexed tokenId_, uint256 renewTime_);
    //    event AddIssua(address owner_,uint256 addIssuaSupply_);
    event WriteOff(address tokenOwner_,uint256 index_, uint256 indexed tokenId_, uint256 supply_);

    // ------------------------- 交易方法 ----------------------------

    /**
      * @dev 设置初始有效期
      * @param initialDeadline_ 初始有效期
      */
//    function setInitialDeadline(uint256 initialDeadline_) public onlyOwner returns (bool){
//        require(_initialDeadline == 0,"NFT.sol --- setInitialDeadline --- _initialDeadline != 0!!!");
//        _initialDeadline = initialDeadline_;
//        return true;
//    }

    /**
      * @dev 铸造NFT
      * @param tokenURI_ NFT显示图片链接
      */
    function mint(string memory tokenURI_) public onlyOwner returns (uint256){
        require(_tokenMinted + 1 <= _totalSupply,"NFT.sol --- mint --- more than totalSupply!!!");
        uint256 tokenId = _tokenMinted + 1;
        super._safeMint(_msgSender(),tokenId);
        _tokenMinted = _tokenMinted.add(1);
        super._setTokenURI(tokenId,tokenURI_);

        _expirationTime[tokenId] = _initialDeadline;

        if(_canWriteOff){
            require(addNew(tokenId),"NFT.sol --- mint --- addNew fail!!!");
        }

         return tokenId;
       
    }

    /**
      * @dev 批量铸造NFT
      * @param supply_ 铸造数量
      * @param tokenURI_ NFT显示图片链接
      */
    function batchMint(uint256 supply_, string memory tokenURI_) public onlyOwner returns (uint256[] memory){
        require(_tokenMinted + supply_ <= _totalSupply,"NFT.sol --- batchMint --- supply_ more than totalSupply!!!");
        require(supply_ <= BATCH_NUM,"NFT.sol --- batchMint --- supply_ more than BATCH_NUM!!!");
        uint256[] memory tokenIds = new uint256[](BATCH_NUM);
        for(uint8 i = 0 ; i < supply_ ; i++){
            uint256 tokenId = mint(tokenURI_);
            tokenIds[i] = tokenId;
        }
        return tokenIds;
    }


    /**
      * @dev 转账
      * @param tokenId_ tokenId
      * @param to_  购买者Address
      */
    function transfer(uint256 tokenId_, address to_) public returns (bool){
        require(ownerOf(tokenId_) == _msgSender() ,"NFT.sol --- transfer --- not the token owner!!!");
        require(to_ != address(0),"NFT.sol --- transfer --- to_ is error!!!");
        require(to_ != _msgSender(),"NFT.sol --- transfer --- to_ can't be the token owner!!!");
        safeTransferFrom(_msgSender(),to_,tokenId_);
        return true;
    }

    /**
     * @dev 批量转账
      * @param tokenIds_ tokenIds
      * @param to_  购买者Address
      */
    function batchTransfer(uint256[] memory tokenIds_, address to_) public returns (bool){
        require(tokenIds_.length <= BATCH_NUM ,"NFT.sol --- batchTransfer --- tokenIds_.length too big!!!");
        bool flag;
        for(uint256 i = 0; i < tokenIds_.length; i++){
            flag = transfer(tokenIds_[i],to_);
        }
        return flag;
    }


    /**
      * @dev 出售NFT
      * @param tokenId_ tokenId
      * @param to_  购买者Address
      * @param expirationTime_ 有效截至时间
      */
    function sell(uint256 tokenId_, address to_, uint256 expirationTime_) public onlyOwner returns (bool){
        require(ownerOf(tokenId_) == _msgSender() ,"NFT.sol --- sell --- not the token owner!!!");
        require(to_ != address(0),"NFT.sol --- sell --- to_ is error!!!");
        require(to_ != _msgSender(),"NFT.sol --- sell --- to_ can't be the token owner!!!");
        if(_canRenew){
            require(expirationTime_ != 0,"NFT.sol --- sell --- expirationTime_ is error!!!");
            _expirationTime[tokenId_] = expirationTime_;
        }
        safeTransferFrom(_msgSender(),to_,tokenId_);
        return true;
    }

    /**
      * @dev 批量出售NFT
      * @param tokenIds_ tokenIds
      * @param to_  购买者Address
      * @param expirationTime_ 有效截至时间
      */
    function batchSell(uint256[] memory tokenIds_, address to_, uint256 expirationTime_) public onlyOwner returns (bool){
        require(tokenIds_.length <= BATCH_NUM ,"NFT.sol --- batchSell --- tokenIds_.length too big!!!");
        bool flag;
        for(uint256 i = 0; i < tokenIds_.length; i++){
            flag = sell(tokenIds_[i],to_,expirationTime_);
        }
        return flag;
    }

    /**
      * @dev 续费
      * @param tokenId_ tokenId
      * @param renewTime_ 续费截至时间
      */
    function renew(uint256 tokenId_, uint256 renewTime_) public virtual onlyOwner returns (bool){
        require(_canRenew,"NFT.sol --- renew --- can't renew!!!");
        require(_exists(tokenId_),"NFT.sol --- renew --- tokenId_ should be exists!!!");
        require(renewTime_ > _expirationTime[tokenId_],"NFT.sol --- renew --- renewTime_ should > _expirationTime[tokenId_] !!!");
        _expirationTime[tokenId_] = renewTime_;
        emit Renew(_msgSender(), tokenId_, renewTime_);
        return true;
    }

    //    /**
    //      * @dev 增发
    //      * @param addIssuaSupply_ tokenId
    //      */
    //    function addIssua(uint256 addIssuaSupply_) public virtual onlyOwner returns (bool){
    //        require(_canIssua,"NFT.sol --- addIssua --- can't addIssua!!!");
    //        require(addIssuaSupply_ > 0 ,"NFT.sol --- addIssua ---addIssuaSupply_ should more than 0!!!");
    //        _totalSupply = _totalSupply.add(addIssuaSupply_);
    //        emit AddIssua(_msgSender(), addIssuaSupply_);
    //        return true;
    //    }

    /**
      * @dev 设置核销信息
      * @param types_ 核销类型
      * @param Supply_ 核销数量
      */
    function setWriteOff(uint256[] memory types_,uint256[] memory Supply_) public onlyOwner returns (bool){
        require(_canWriteOff,"NFT.sol --- setWriteOff --- can't writeOff!!!");
        require(types_.length == Supply_.length && types_.length != 0, "NFT.sol --- setWriteOff --- types_.length should be same as vipSupply_.length and should more than 0!!!");
        bool flag;
        for(uint256 i = 0; i < types_.length; i++){
            flag = setWriteOff(types_[i],Supply_[i]);
        }
        return flag;
    }

    /**
      * @dev 核销
      * @param type_ 核销类型
      * @param tokenId_ tokenId
      * @param supply_ 核销数量
      */
    function writeOff(uint256 type_, uint256 tokenId_, uint256 supply_) public virtual override returns (bool){
        require(owner() != _msgSender(),"NFT.sol --- writeOff --- manager can't writeOff!!! ");
        require(_isApprovedOrOwner(_msgSender(), tokenId_),"NFT.sol --- writeOff --- caller is not owner nor approved!!");
        require(block.timestamp <= _expirationTime[tokenId_],"NFT.sol --- writeOff --- Membership expired!!!");
        require(_canWriteOff,"NFT.sol --- writeOff --- can't writeOff!!!");
        require(supply_ != 0,"NFT.sol --- writeOff --- supply_ should more than 0!!!");
        bool result = WriteOffable.writeOff(type_,tokenId_,supply_);
        emit WriteOff(_msgSender(), type_, tokenId_, supply_);
        return result;
    }

    /**
      * @dev 销毁
      * @param tokenId_ tokenId
      */
    function burn(uint256 tokenId_) public virtual returns (bool){
        require(_isApprovedOrOwner(_msgSender(), tokenId_), "NFT.sol --- burn --- caller is not owner nor approved");
        require(_exists(tokenId_), "NFT.sol --- burn --- tokenId_ is not exists");
        //删除核销数量
        for(uint256 i = 0; i < _types.length; i++){
           _tokenVipSupply[tokenId_][_types[i]] = 0;
        }
        super._burn(tokenId_);
        return true;
    }

    /**
      * @dev 批量销毁
      * @param tokenIds_ tokenIds
      */
    function batchBurn(uint256[] memory tokenIds_) public virtual returns (bool){
        bool flag;
        for(uint256 i = 0; i < tokenIds_.length; i++){
            flag = burn(tokenIds_[i]);
        }
        return flag;
    }

    // ------------------------- 查询方法 ----------------------------
    /**
      * @dev 查询NFT允许铸造的最大数量
      */
    function getTotalSupply() public view returns (uint256){
        return _totalSupply;
    }

    /**
      * @dev 查询是否可续费
      */
    function getCanRenew() public view returns (bool){
        return _canRenew;
    }

    /**
      * @dev 查询是否支持增发
      */
    // function getCanIssua() public view returns (bool){
    //     return _canIssua;
    // }

    /**
      * @dev 查询是否支持核算
      */
    function getCanWriteOff() public view returns (bool){
        return _canWriteOff;
    }

    /**
      * @dev 查询会员到期时间
      * @param tokenId_ tokenId
      */
    function getExpirationTime(uint256 tokenId_) public view returns (uint256){
        return _expirationTime[tokenId_];
    }

    /**
      * @dev 查询权益链接
      */
    function getEquityLink() public view returns (string memory){
        return _equityLink;
    }

    /**
      * @dev 查询已经铸造的数量
      */
    function getTokenMinted() public view returns (uint256){
        return _tokenMinted;
    }

    /**
      * @dev 查询token是否过期
      * @param tokenId_ tokenId
      */
    function getOverTime(uint256 tokenId_) public view returns (bool){
        return block.timestamp > _expirationTime[tokenId_];
    }

    /**
      * @dev 查询链上时间戳
      */
    function getNow() public view returns (uint256){
        return block.timestamp;
    }

    /**
      * @dev 查询地址拥有地址数
      */
    function getTokens(address owner_) public view returns (uint256[] memory){
        return _ownedTokensArr[owner_];
    }

}