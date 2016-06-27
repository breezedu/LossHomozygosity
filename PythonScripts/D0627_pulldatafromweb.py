# -*- coding: utf-8 -*-
"""
Created on Mon Jun 27 00:41:49 2016

@author: Jeff
"""
def downloadCSV( geneName ):
    
    
    ## import webdriver
    from splinter.browser import Browser
    
    ## alternative driver api
    ## driver = webdriver.Chrome("C:/Users/Jeff/Downloads/chromedriver.exe")
    ## import the driver, and visit the exac.broadinstitute.org webpage
    ## b = driver.get("http://exac.broadinstitute.org/awesome?query=SAMD11") 
    
    
    ## load chrome driver, visit targer url
    b = Browser(driver_name="chrome")
    url = "http://exac.broadinstitute.org/awesome?query=" + geneName
    
    b.visit(url)
    
    
    ## find the LoF button, by web elementary inspect
    button = b.find_by_id(u"consequence_lof_variant_button")
    
    ## trigger the button
    button.click()
    
    
    ## find the Export to CSV button, also by web elementary inspect
    button2 = b.find_by_id(u"export_to_csv")
    
    ## trigger the button, download the target CSV document
    button2.click()
    
    b.quit()
    ## repeat~~~
    ## b.visit("http://exac.broadinstitute.org/awesome?query=PCSK9")
    

downloadCSV("SAMD11")
downloadCSV("TNN")
