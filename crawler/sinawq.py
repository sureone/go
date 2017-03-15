import scrapy
import sqlite3
import re
import sys
    

class QuotesSpider(scrapy.Spider):
    name = "sgfs"
    start_urls = [
        'http://duiyi.sina.com.cn/gibo/sanxing_gibo.asp?cur_page=8&key=2&keyword=%C8%FD%D0%C7',
        'http://duiyi.sina.com.cn/gibo/sanxing_gibo.asp?cur_page=9&key=2&keyword=%C8%FD%D0%C7',
        'http://duiyi.sina.com.cn/gibo/sanxing_gibo.asp?cur_page=10&key=2&keyword=%C8%FD%D0%C7',
        'http://duiyi.sina.com.cn/gibo/sanxing_gibo.asp?cur_page=11&key=2&keyword=%C8%FD%D0%C7',
        'http://duiyi.sina.com.cn/gibo/sanxing_gibo.asp?cur_page=12&key=2&keyword=%C8%FD%D0%C7',
        'http://duiyi.sina.com.cn/gibo/sanxing_gibo.asp?cur_page=13&key=2&keyword=%C8%FD%D0%C7',
        'http://duiyi.sina.com.cn/gibo/sanxing_gibo.asp?cur_page=14&key=2&keyword=%C8%FD%D0%C7'
    ]
    cx = sqlite3.connect("../src/sql.db")
    def parse(self, response):
        cu = self.cx.cursor()
        for quote in response.xpath('//table//table//table//table//table//table//table//table//tr[1]/following-sibling::*'):
            data = {
                'date': quote.xpath('td[1]//div/text()').extract_first(),
                'black': quote.xpath('td[2]//a//div/text()').extract_first().encode('utf-8'),
                'white': quote.xpath('td[3]//a//div/text()').extract_first().encode('utf-8'),
                'name': quote.xpath('td[4]//a//div/text()').extract_first().encode('utf-8'),
                'sgf': quote.xpath('td[4]//a/@href').extract_first(),
                'result': quote.xpath('td[5]//div/text()').extract_first().encode('utf-8'),
            }
            p = r"JavaScript:gibo_load\(\'(http://.*\.sgf)\'\);"
            pp = re.compile(p);
            data['sgf']=pp.findall(data['sgf'])[0]
            sql = "insert into sgfs(name,black,white,sgf,result,cdate) values('{:s}','{:s}','{:s}','{:s}','{:s}','{:s}')".format(data['name'],data['black'],data['white'],data['sgf'],data['result'],data['date'])
            cu.execute(sql)
        self.cx.commit()
