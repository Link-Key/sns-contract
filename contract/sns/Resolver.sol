// SPDX-License-Identifier: MIT
pragma solidity >=0.8.4;
pragma experimental ABIEncoderV2;

/**
 * A generic resolver interface which includes all the functions including the ones deprecated
 */
interface Resolver {

    event ContentChanged(string indexed name_, string type_, string newContent);
    event OwnerChanged(string indexed name_, address newOwner);

    function setRecords(string memory name_, address owner_) external;

    function getEthAddress(string memory name_) external view returns (string memory);

    function setEthAddress(string memory name_, string memory ethAddress_) external;

    function getBtcAddress(string memory name_) external view returns (string memory);

    function setBtcAddress(string memory name_, string memory btcAddress_) external;

    function getLtcAddress(string memory name_) external view returns (string memory);

    function setLtcAddress(string memory name_, string memory ltcAddress_) external;

    function getDogeAddress(string memory name_) external view returns (string memory);

    function setDogeAddress(string memory name_, string memory dogeAddress_) external;

    function getIpfsUrl(string memory name_) external view returns (string memory);

    function setIpfsUrl(string memory name_, string memory ipfsUrl_) external;

    function getUrl(string memory name_) external view returns (string memory);

    function setUrl(string memory name_, string memory url_) external;

    function getEmail(string memory name_) external view returns (string memory);

    function setEmail(string memory name_, string memory email_) external;

    function getAvatar(string memory name_) external view returns (string memory);

    function setAvatar(string memory name_, string memory avatar_) external;

    function getDescription(string memory name_) external view returns (string memory);

    function setDescription(string memory name_, string memory description_) external;

    function getNotice(string memory name_) external view returns (string memory);

    function setNotice(string memory name_, string memory notice_) external;

    function getKeywords(string memory name_) external view returns (string memory);

    function setKeywords(string memory name_, string memory keywords_) external;

    function getComGithub(string memory name_) external view returns (string memory);

    function setComGithub(string memory name_, string memory comGithub_) external;

    function getComReddit(string memory name_) external view returns (string memory);

    function setComReddit(string memory name_, string memory comReddit_) external;

    function getComTwitter(string memory name_) external view returns (string memory);

    function setComTwitter(string memory name_, string memory comTwitter_) external;

    function getOrgTelegram(string memory name_) external view returns (string memory);

    function setOrgTelegram(string memory name_, string memory orgTelegram_) external;

    function getOrgTelegram(string memory name_) external view returns (string memory);

    function getAllProperties(string memory name_) external view returns (string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory, string memory);

}
