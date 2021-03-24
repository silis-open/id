--(c)http://gitee.com/silis-open/id

DROP FUNCTION [dbo].[Id_GenerateChar]
GO

CREATE FUNCTION [dbo].[Id_GenerateChar]
(
	@rank FLOAT
)
RETURNS CHAR(24)
AS
BEGIN
	RETURN CONVERT(CHAR(24),
       CONVERT(VARBINARY(4), DATEDIFF(SECOND,'1970-01-01 00:00:00', GETUTCDATE())) +
       CONVERT(VARBINARY(6), @rank) + 
	   CONVERT(VARBINARY(2), @@SPID ^ @@CPU_BUSY ^ @@IDLE), 2)
END