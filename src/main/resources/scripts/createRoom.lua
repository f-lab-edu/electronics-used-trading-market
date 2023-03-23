local buyerNo = KEYS[1]
local sellerNo = KEYS[2]
local roomId = ARGV[1]

redis.call("SADD", buyerNo, roomId)
redis.call("SADD", sellerNo, roomId)

return roomId